package com.humanmusik.cleanhome.presentation.authentication.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.humanmusik.cleanhome.domain.model.authentication.AuthState
import com.humanmusik.cleanhome.presentation.authentication.EmailTextField
import com.humanmusik.cleanhome.presentation.authentication.PasswordTextField

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onSignUpNavigation: () -> Unit,
    onSuccessfulLoginNavigation: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LoginContent(
        state = state,
        onEmailValueChanged = viewModel::onEmailValueChanged,
        onPasswordValueChanged = viewModel::onPasswordValueChanged,
        onLoginWithEmailAndPassword = { viewModel.onLoginWithEmailAndPassword(onSuccessfulLoginNavigation) },
        onSignUpPressed = { viewModel.onSignUpPressed(onSignUpNavigation) },
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
    )
}

@Composable
fun LoginContent(
    state: LoginState,
    onEmailValueChanged: (String) -> Unit,
    onPasswordValueChanged: (String) -> Unit,
    onLoginWithEmailAndPassword: () -> Unit,
    onSignUpPressed: () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingValues())
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            EmailTextField(
                textFieldValue = state.email,
                isError = state.emailError,
                onEmailChanged = onEmailValueChanged,
            )

            PasswordTextField(
                textFieldValue = state.password,
                isError = state.passwordError,
                onPasswordChanged = onPasswordValueChanged,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
            )

            LoginButton(
                authState = state.authState,
                isEnabled = state.loginButtonEnabled,
                onLogin = onLoginWithEmailAndPassword,
            )

            SignUpButton(
                onSignUpPressed = onSignUpPressed,
            )
        }
    }
}

@Composable
private fun LoginButton(
    authState: AuthState,
    isEnabled: Boolean,
    onLogin: () -> Unit,
) {
    Button(
        onClick = onLogin,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        enabled = isEnabled,
    ) {
        if (authState == AuthState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        } else {
            Text("Login")
        }
    }
}

@Composable
private fun SignUpButton(
    onSignUpPressed: () -> Unit,
) {
    TextButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        onClick = onSignUpPressed,
    ) {
        Text("Sign Up")
    }
}

@Preview
@Composable
fun Preview_LoginScreen() {
    LoginContent(
        state = LoginState(
            email = "",
            password = "",
            isPasswordVisible = true,
            authState = AuthState.Unauthenticated,
            emailError = false,
            passwordError = false,
            loginButtonEnabled = true,
        ),
        onEmailValueChanged = { },
        onPasswordValueChanged = { },
        onLoginWithEmailAndPassword = { },
        onSignUpPressed = { },
        onTogglePasswordVisibility = { },
    )
}