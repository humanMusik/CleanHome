package com.humanmusik.cleanhome.data.network.user

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.NetworkException
import com.humanmusik.cleanhome.data.repository.auth.GetUserId
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_FIRESTORE_COLLECTION_ID = "users"
private const val TAG = "FirestoreUserApi"
interface UserApi {
    suspend fun addUser(user: User)
    suspend fun listHomes(userId: GetUserId.UserId): List<String>
}

@Singleton
class FirestoreUserApi @Inject constructor(
    firestore: FirebaseFirestore,
) : UserApi {
    val firestoreUsersCollectionRef = firestore.collection(USER_FIRESTORE_COLLECTION_ID)

    override suspend fun addUser(user: User) {
        try {
            if (user.id == null) throw IllegalStateException("User Id was not provided")

            firestoreUsersCollectionRef
                .document(user.id)
                .set(user)
                .await()

            Log.d(TAG, "User uploaded to firestore")
        } catch (e: Throwable) {
            throw NetworkException(e.message ?: "unknown network error occurred")
        }
    }

    override suspend fun listHomes(userId: GetUserId.UserId): List<String> {
        return try {
            val document = firestoreUsersCollectionRef
                .document(userId.value)
                .get()
                .await()

            val user = document.toObject(User::class.java)
            user?.homes ?: emptyList()
        } catch (e: Throwable) {
            throw NetworkException(e.message ?: "unknown network error occurred")
        }
    }
}
