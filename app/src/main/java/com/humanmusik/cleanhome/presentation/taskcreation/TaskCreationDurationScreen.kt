package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.time.Duration

@Composable
fun TaskCreationDurationScreen(
    viewModel: TaskCreationDurationViewModel,
    onCreateTaskNavigation: () -> Unit,
) {
    TaskCreationDurationContent(
        onCreateTask = { duration ->
            viewModel.onCreateTask(
                duration = duration,
                navigation = onCreateTaskNavigation,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreationDurationContent(
    onCreateTask: (Duration) -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(text = "Task Duration")

            val durationState = rememberTimePickerState(is24Hour = true)
            TimeInput(state = durationState)

            Button(onClick = { onCreateTask(Duration.parse("PT${durationState.hour}H${durationState.minute}M")) }) {
                Text("Create Task")
            }
        }
    }
}