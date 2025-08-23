package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableIntStateOf
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskParcelData

@Composable
fun TaskCreationDateFreqUrgencyScreen(
    viewModel: TaskCreationDateFreqUrgencyViewModel,
    onContinue: (TaskParcelData) -> Unit,
) {
    TaskCreationDateFreqUrgencyContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCreationDateFreqUrgencyContent() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(text = "Date")
            val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
            DatePicker(state = state)

            Text(text = "Frequency")

            val frequencies: List<String> = listOf(
                Frequency.Daily.name,
                Frequency.Weekly.name,
                Frequency.Fortnightly.name,
                Frequency.Monthly.name,
                Frequency.Quarterly.name,
                Frequency.BiAnnually.name,
                Frequency.Annually.name,
            )
            var expanded by remember { mutableStateOf(false) }
            val frequencyTextField = rememberTextFieldState(frequencies[0])

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                TextField(
                    // The `menuAnchor` modifier must be passed to the text field to handle
                    // expanding/collapsing the menu on click. A read-only text field has
                    // the anchor type `PrimaryNotEditable`.
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    state = frequencyTextField,
                    readOnly = true,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text("Label") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    frequencies.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                frequencyTextField.setTextAndPlaceCursorAtEnd(option)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            Text(text = "Urgency")
            var selectedIndex by remember { mutableIntStateOf(0) }
            val options = listOf("Non-Urgent", "Urgent")
            SingleChoiceSegmentedButtonRow {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                    ) {
                        Text(label)
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