package com.humanmusik.cleanhome.domain.model.authentication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface AuthState : Parcelable {
    data object Authenticated : AuthState
    data object Unauthenticated : AuthState
    data object Loading : AuthState
    data class Error(val exception: AuthException) : AuthState

    fun isLoading() = this == Loading
}

open class AuthException(override val message: String?): Throwable()
class FirebaseSignInException(override val message: String?): AuthException(message)
class FirebaseCreateUserException(override val message: String?): AuthException(message)
