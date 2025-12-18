package com.humanmusik.cleanhome.presentation.authentication.registration

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.data.repository.auth.AuthRepository
import com.humanmusik.cleanhome.domain.model.User
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.isLoading
import com.humanmusik.cleanhome.presentation.onLoading
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.util.doNotSaveState
import com.humanmusik.cleanhome.util.savedStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    var currentJob: Job? = null
    val state = savedStateFlow(
        savedStateBehaviour = doNotSaveState(),
        initialState = RegistrationState(
            email = "",
            firstName = "",
            lastName = "",
            password = "",
            isPasswordVisible = false,
            firstNameError = false,
            lastNameError = false,
            passwordError = false,
            emailError = false,
            registerButtonEnabled = false,
            userCreationState = FlowState.Idle(),
        )
    )

    fun onRegisterPressed(navigation: () -> Unit) {
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            FlowState
                .fromSuspendingFunc {
                    authRepository.createUserWithEmailAndPassword(
                        user = User(
                            email = state.value.email,
                            firstName = state.value.firstName,
                            lastName = state.value.lastName,
                        ),
                        password = state.value.password
                    )
                }
                .onEach { result -> state.update { it.copy(userCreationState = result) } }
                .onLoading { checkToEnableRegisterButton() }
                .onSuccess { navigation() }
                .launchIn(viewModelScope)
        }
    }

    fun onFirstNameValueChanged(newValue: String) {
        state.update {
            it.copy(
                firstName = newValue,
                firstNameError = newValue.isEmpty(),
            )
        }
    }

    fun onLastNameValueChanged(newValue: String) {
        state.update {
            it.copy(
                lastName = newValue,
                lastNameError = newValue.isEmpty(),
            )
        }
    }

    fun onEmailValueChanged(newValue: String) {
        state.update {
            it.copy(
                email = newValue,
                emailError = newValue.isEmpty()
                        || !Patterns.EMAIL_ADDRESS.matcher(newValue).matches(),
            )
        }
        checkToEnableRegisterButton()
    }

    fun onPasswordValueChanged(newValue: String) {
        state.update {
            it.copy(
                password = newValue,
                passwordError = newValue.length < 6,
            )
        }
        checkToEnableRegisterButton()
    }

    fun onTogglePasswordVisibility() {
        state.update { it.copy(isPasswordVisible = !state.value.isPasswordVisible) }
    }

    fun onEmailExistsAcknowledged() {
        state.update {
            it.copy(
                email = "",
                userCreationState = FlowState.Idle(),
            )
        }
    }

    fun onRegistrationErrorCancelled() {
        state.update { it.copy(userCreationState = FlowState.Idle()) }
    }

    private fun checkToEnableRegisterButton() {
        state.update {
            it.copy(
                registerButtonEnabled = state.value.let { state ->
                    !state.firstNameError
                            && !state.emailError
                            && !state.lastNameError
                            && !state.passwordError
                            && !state.userCreationState.isLoading()
                }
            )
        }
    }
}
