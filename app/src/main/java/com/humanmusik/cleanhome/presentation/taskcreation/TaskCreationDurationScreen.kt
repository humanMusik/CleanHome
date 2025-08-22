package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TaskCreationDurationScreen() {
    TaskCreationDurationContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreationDurationContent() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(text = "Task Duration")

            val durationState = rememberTimePickerState(is24Hour = true)
            TimeInput(state = durationState,)
        }
    }
}