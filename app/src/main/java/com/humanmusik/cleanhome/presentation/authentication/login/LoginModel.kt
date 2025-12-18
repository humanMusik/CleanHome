package com.humanmusik.cleanhome.presentation.authentication.login

import android.os.Parcelable
import com.humanmusik.cleanhome.domain.model.authentication.AuthState
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginState(
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val emailError: Boolean,
    val passwordError: Boolean,
    val loginButtonEnabled: Boolean,
    val authState: AuthState,
) : Parcelable