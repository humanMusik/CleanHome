package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TaskCreationNameAndRoomScreen() {
    TaskCreationNameAndRoomContent()
}

@Composable
private fun TaskCreationNameAndRoomContent() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(text = "Task Name")

            TextField(
                state = rememberTextFieldState(),
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text("Task Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                inputTransformation = InputTransformation.maxLength(20),
            )

            Text(text = "Room")

            TextField(
                state = rememberTextFieldState(),
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text("Room") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                inputTransformation = InputTransformation.maxLength(20),
            )

        }
    }

}