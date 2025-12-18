package com.humanmusik.cleanhome.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.humanmusik.cleanhome.data.mappers.toFirestoreUserModel
import com.humanmusik.cleanhome.data.network.user.UserApi
import com.humanmusik.cleanhome.di.ApplicationScope
import com.humanmusik.cleanhome.domain.model.User
import com.humanmusik.cleanhome.domain.model.authentication.AuthException
import com.humanmusik.cleanhome.domain.model.authentication.AuthState
import com.humanmusik.cleanhome.domain.model.authentication.FirebaseCreateUserException
import com.humanmusik.cleanhome.domain.model.authentication.FirebaseSignInException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

fun interface CheckUserSession {
    fun checkUserSession(): Flow<AuthState>

    companion object {
        operator fun CheckUserSession.invoke() = checkUserSession()
    }
}

fun interface GetUserId {
    fun getUserId(): UserId

    companion object {
        operator fun GetUserId.invoke() = getUserId()
    }

    @JvmInline
    value class UserId(val value: String)
}

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String)
    suspend fun createUserWithEmailAndPassword(user: User, password: String)
}

class FirebaseAuthRepository @Inject constructor(
    @param:ApplicationScope private val scope: CoroutineScope,
    val firebaseAuth: FirebaseAuth,
    val userApi: UserApi,
) : AuthRepository,
    CheckUserSession,
    GetUserId {
    override fun checkUserSession(): Flow<AuthState> {
        return callbackFlow {
            val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                val authState = if (firebaseAuth.currentUser == null) {
                    AuthState.Unauthenticated
                } else {
                    AuthState.Authenticated
                }

                // Emit the current auth status to the flow
                trySend(authState)
            }

            // Add the listener when the flow starts collecting
            firebaseAuth.addAuthStateListener(authStateListener)

            // The awaitClose block is executed when the flow is cancelled or closed
            // This is crucial for resource cleanup and preventing memory leaks
            awaitClose {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }
    }

    override fun getUserId(): GetUserId.UserId {
        val userId = firebaseAuth.currentUser?.uid ?: throw AuthException("Unauthenticated")
        return GetUserId.UserId(userId)
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        try {
            firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
        } catch (e: Throwable) {
            throw FirebaseSignInException(
                e.message ?: "Sign in failed without exception message",
            )
        }
    }

    override suspend fun createUserWithEmailAndPassword(user: User, password: String) {
        try {
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(user.email, password)
                .await()

            val userId = authResult.user?.uid
                ?: throw FirebaseCreateUserException("User creation succeeded but user is null")

            userApi.addUser(user.copy(id = userId).toFirestoreUserModel())
        } catch (exception: Throwable) {
            when (exception) {
                is FirebaseAuthUserCollisionException -> throw exception
                else -> throw FirebaseCreateUserException(exception.message)
            }
        }
    }
}
