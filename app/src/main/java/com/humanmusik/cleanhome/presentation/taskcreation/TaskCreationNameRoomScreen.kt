package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskCreationNameRoomState
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskParcelData

@Composable
fun TaskCreationNameRoomScreen(
    viewModel: TaskCreationNameRoomViewModel = hiltViewModel(),
    onContinueNavigation: (TaskParcelData) -> Unit,
) {
    val state = viewModel.stateFlow.collectAsState()

    state.value.onSuccess {
        TaskCreationNameRoomContent(
            state = it,
            onContinue = { taskName, roomName ->
                viewModel.onContinue(
                    taskName = taskName,
                    roomName = roomName,
                ) { taskParcelData ->
                    onContinueNavigation(taskParcelData)
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCreationNameRoomContent(
    state: TaskCreationNameRoomState,
    onContinue: (String, String) -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(text = "Task Name")

            val nameTextFieldState = rememberTextFieldState()
            TextField(
                state = nameTextFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = { Text("Task Name") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                inputTransformation = InputTransformation.maxLength(20),
            )

            Text(text = "Room")

            val options: List<String> = state.allRooms.map { it.name }
            var expanded by remember { mutableStateOf(false) }
            val roomTextFieldState = rememberTextFieldState()

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                TextField(
                    // The `menuAnchor` modifier must be passed to the text field to handle
                    // expanding/collapsing the menu on click. A read-only text field has
                    // the anchor type `PrimaryNotEditable`.
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    state = roomTextFieldState,
                    readOnly = true,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text("Label") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                roomTextFieldState.setTextAndPlaceCursorAtEnd(option)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            Button(
                onClick = {
                    onContinue(
                        nameTextFieldState.text.toString(),
                        roomTextFieldState.text.toString(),
                    )
                },
            ) {
                Text("Continue")
            }
        }
    }

}