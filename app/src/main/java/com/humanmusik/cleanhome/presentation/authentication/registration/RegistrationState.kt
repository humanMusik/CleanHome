package com.humanmusik.cleanhome.presentation.authentication.registration

import android.os.Parcelable
import com.humanmusik.cleanhome.presentation.FlowState
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegistrationState(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val firstNameError: Boolean,
    val lastNameError: Boolean,
    val emailError: Boolean,
    val passwordError: Boolean,
    val registerButtonEnabled: Boolean,
    val userCreationState: FlowState<Unit>,
) : Parcelable
