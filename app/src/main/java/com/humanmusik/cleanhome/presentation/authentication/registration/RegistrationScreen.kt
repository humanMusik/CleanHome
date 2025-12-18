package com.humanmusik.cleanhome.presentation.authentication.registration

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.authentication.EmailTextField
import com.humanmusik.cleanhome.presentation.authentication.PasswordTextField
import com.humanmusik.cleanhome.presentation.isLoading
import com.humanmusik.cleanhome.presentation.onFailure

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onRegisterNavigation: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    RegistrationContent(
        state = state,
        onFirstNameValueChanged = viewModel::onFirstNameValueChanged,
        onLastNameValueChanged = viewModel::onLastNameValueChanged,
        onEmailValueChanged = viewModel::onEmailValueChanged,
        onPasswordValueChanged = viewModel::onPasswordValueChanged,
        onRegisterPressed = { viewModel.onRegisterPressed(onRegisterNavigation) },
        onEmailExistsAcknowledged = viewModel::onEmailExistsAcknowledged,
        onRegistrationErrorCancelled = viewModel::onRegistrationErrorCancelled,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
    )
}

@Composable
private fun RegistrationContent(
    state: RegistrationState,
    onFirstNameValueChanged: (String) -> Unit,
    onLastNameValueChanged: (String) -> Unit,
    onEmailValueChanged: (String) -> Unit,
    onPasswordValueChanged: (String) -> Unit,
    onRegisterPressed: () -> Unit,
    onEmailExistsAcknowledged: () -> Unit,
    onRegistrationErrorCancelled: () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
) {
    state.userCreationState.onFailure { throwable ->
        when (throwable) {
            is FirebaseAuthUserCollisionException -> {
                RegistrationErrorDialog(
                    dialogText = "Email already exists.",
                    positiveButtonText = "OK",
                    negativeButton = { },
                    onPositiveButtonPressed = onEmailExistsAcknowledged,
                    onNegativeButtonPressed = { },
                )
            }

            else -> {
                RegistrationErrorDialog(
                    dialogText = "Something went wrong.",
                    positiveButtonText = "Try Again",
                    negativeButton = { onNegativeButtonPressed ->
                        TextButton(
                            onClick = onNegativeButtonPressed,
                        ) {
                            Text("Cancel")
                        }
                    },
                    onPositiveButtonPressed = onRegisterPressed,
                    onNegativeButtonPressed = onRegistrationErrorCancelled,
                )
            }
        }
    }

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

            FirstNameTextField(
                textFieldValue = state.firstName,
                isError = state.firstNameError,
                onFirstNameChanged = onFirstNameValueChanged,
            )

            LastNameTextField(
                textFieldValue = state.lastName,
                isError = state.lastNameError,
                onLastNameChanged = onLastNameValueChanged,
            )

            PasswordTextField(
                textFieldValue = state.password,
                onPasswordChanged = onPasswordValueChanged,
                isPasswordVisible = state.isPasswordVisible,
                isError = state.passwordError,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
            )

            RegisterButton(
                userCreationState = state.userCreationState,
                isEnabled = state.registerButtonEnabled,
                onRegister = onRegisterPressed,
            )
        }
    }
}

@Composable
private fun FirstNameTextField(
    textFieldValue: String,
    isError: Boolean,
    onFirstNameChanged: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { onFirstNameChanged(it) },
            label = { Text("First Name") },
            placeholder = { Text("Enter your first name") },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = "Cannot be empty.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
    }
}

@Composable
private fun LastNameTextField(
    textFieldValue: String,
    isError: Boolean,
    onLastNameChanged: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { onLastNameChanged(it) },
            label = { Text("Last Name") },
            placeholder = { Text("Enter your last name") },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = "Cannot be empty.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
    }
}

@Composable
private fun RegisterButton(
    userCreationState: FlowState<Unit>,
    isEnabled: Boolean,
    onRegister: () -> Unit,
) {
    Button(
        onClick = onRegister,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        enabled = isEnabled,
    ) {
        if (userCreationState.isLoading()) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        } else {
            Text("Register")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationErrorDialog(
    dialogText: String,
    positiveButtonText: String,
    negativeButton: @Composable RowScope.(() -> Unit) -> Unit,
    onPositiveButtonPressed: () -> Unit,
    onNegativeButtonPressed: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onNegativeButtonPressed,
    ) {
        Surface(
            modifier = Modifier
                .defaultMinSize(
                    minWidth = 100.dp,
                    minHeight = 100.dp,
                ),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(dialogText)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                ) {
                    TextButton(
                        onClick = onPositiveButtonPressed,
                    ) {
                        Text(positiveButtonText)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    negativeButton(onNegativeButtonPressed)
                }
            }
        }
    }
}