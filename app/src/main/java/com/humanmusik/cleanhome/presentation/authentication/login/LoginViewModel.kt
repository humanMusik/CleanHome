package com.humanmusik.cleanhome.presentation.authentication.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.data.repository.auth.AuthRepository
import com.humanmusik.cleanhome.domain.model.authentication.AuthException
import com.humanmusik.cleanhome.domain.model.authentication.AuthState
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.onFailure
import com.humanmusik.cleanhome.presentation.onLoading
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.util.MutableSavedStateFlow
import com.humanmusik.cleanhome.util.doNotSaveState
import com.humanmusik.cleanhome.util.savedStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private var currentJob: Job? = null

    val state: MutableSavedStateFlow<LoginState> = savedStateFlow(
        savedStateBehaviour = doNotSaveState(),
        initialState = LoginState(
            email = "",
            password = "",
            isPasswordVisible = false,
            emailError = false,
            passwordError = false,
            loginButtonEnabled = false,
            authState = AuthState.Unauthenticated,
        ),
    )

    fun onLoginWithEmailAndPassword(navigation: () -> Unit) {
        currentJob?.cancel()

        currentJob = FlowState
            .fromSuspendingFunc {
                authRepository.signInWithEmailAndPassword(
                    state.value.email,
                    state.value.password
                )
            }
            .onLoading { state.update { it.copy(authState = AuthState.Loading) } }
            .onSuccess {
                state.update { it.copy(authState = AuthState.Authenticated) }
                navigation()
            }
            .onFailure { throwable ->
                state.update { it.copy(authState = AuthState.Error(throwable as AuthException)) }
            }
            .launchIn(viewModelScope)
    }

    fun onSignUpPressed(navigation: () -> Unit) {
        navigation()
    }

    fun onEmailValueChanged(newValue: String) {
        state.update {
            it.copy(
                email = newValue,
                emailError = newValue.isEmpty()
                        || !Patterns.EMAIL_ADDRESS.matcher(newValue).matches(),
            )
        }
        checkToEnableLoginButton()
    }

    fun onPasswordValueChanged(newValue: String) {
        state.update {
            it.copy(
                password = newValue,
                passwordError = newValue.length < 6,
            )
        }
        checkToEnableLoginButton()
    }

    fun onTogglePasswordVisibility() {
        state.update { it.copy(isPasswordVisible = !state.value.isPasswordVisible) }
    }

    private fun checkToEnableLoginButton() {
        state.update {
            it.copy(
                loginButtonEnabled = state.value.let { state ->
                    !state.emailError && !state.passwordError && !state.authState.isLoading()
                }
            )
        }
    }
}
