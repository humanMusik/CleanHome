package com.humanmusik.cleanhome.presentation.authentication.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.authentication.EmailTextField
import com.humanmusik.cleanhome.presentation.authentication.PasswordTextField
import com.humanmusik.cleanhome.presentation.isLoading
import com.humanmusik.cleanhome.presentation.utils.composables.AlertDialog
import com.humanmusik.cleanhome.presentation.utils.composables.AlertDialogParams
import com.humanmusik.cleanhome.presentation.utils.composables.AlertDialogState

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
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onEmailExistsAcknowledged = viewModel::onEmailExistsAcknowledged,
        onSomethingWentWrongDismissed = viewModel::onSomethingWentWrongDismissed,
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
    onTogglePasswordVisibility: () -> Unit,
    onEmailExistsAcknowledged: () -> Unit,
    onSomethingWentWrongDismissed: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
//    val scope = rememberCoroutineScope()

    if (state.errorDialog is AlertDialogState.Show) {
        AlertDialog(
            params = state.errorDialog.params,
            onPositiveButtonPressed = {
                when (state.errorDialog.params.key) {
                    userExistsDialogKey -> {
                        onEmailExistsAcknowledged()
                        if (state.emailError) focusRequester.requestFocus()
                    }
                    AlertDialogParams.somethingWentWrongKey -> {
                        onRegisterPressed()
                    }
                    else -> {}
                }
            },
            onNegativeButtonPressed = {
                when (state.errorDialog.params.key) {
                    userExistsDialogKey -> {
                        onEmailExistsAcknowledged()
                        if (state.emailError) focusRequester.requestFocus()
                    }
                    AlertDialogParams.somethingWentWrongKey -> {
                        onSomethingWentWrongDismissed()
                    }
                    else -> {}
                }
            }
        )
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
                focusRequester = focusRequester,
                onEmailChanged = onEmailValueChanged,
            )

            FirstNameTextField(
                textFieldValue = state.firstName,
                isError = state.firstNameError,
                focusManager = focusManager,
                onFirstNameChanged = onFirstNameValueChanged,
            )

            LastNameTextField(
                textFieldValue = state.lastName,
                isError = state.lastNameError,
                focusManager = focusManager,
                onLastNameChanged = onLastNameValueChanged,
            )

            PasswordTextField(
                textFieldValue = state.password,
                onPasswordChanged = onPasswordValueChanged,
                isPasswordVisible = state.isPasswordVisible,
                isError = state.passwordError,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
                onKeyboardDone = onRegisterPressed,
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
    focusManager: FocusManager,
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
    }
}

@Composable
private fun LastNameTextField(
    textFieldValue: String,
    isError: Boolean,
    focusManager: FocusManager,
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
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
