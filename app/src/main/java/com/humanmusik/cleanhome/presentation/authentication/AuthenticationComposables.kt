package com.humanmusik.cleanhome.presentation.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.cleanhome.R

@Composable
internal fun EmailTextField(
    textFieldValue: String,
    isError: Boolean,
    onEmailChanged: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { onEmailChanged(it) },
            label = { Text("Email") },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = "Invalid email.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            },
            placeholder = { Text("Enter your email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
    }
}

@Composable
internal fun PasswordTextField(
    textFieldValue: String,
    isPasswordVisible: Boolean,
    isError: Boolean,
    onPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            onValueChange = { onPasswordChanged(it) },
            label = { Text("Password") },
            placeholder = { Text("Enter your password") },
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = "Must be at least 6 characters.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        painter = painterResource(
                            if (isPasswordVisible) R.drawable.ic_eye_open else R.drawable.ic_eye_closed
                        ),
                        contentDescription = "Toggle Password Visibility",
                    )
                }
            },
            singleLine = true,
        )
    }
}