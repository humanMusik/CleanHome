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
import androidx.compose.material3.getSelectedDate
import androidx.compose.runtime.mutableIntStateOf
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.navigation.BackStackInstructor
import java.time.LocalDate

@Composable
fun TaskCreationDateFreqUrgencyScreen(
    viewModel: TaskCreationDateFreqUrgencyViewModel,
    navigation: (BackStackInstructor) -> Unit,
) {
    TaskCreationDateFreqUrgencyContent(
        onContinue = { date, frequency, urgency ->
            navigation(viewModel.onContinue(date, frequency, urgency))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCreationDateFreqUrgencyContent(
    onContinue: (LocalDate?, String, Urgency) -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(text = "Date")
            val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
            DatePicker(state = datePickerState)

            Text(text = "Frequency")

            var expanded by remember { mutableStateOf(false) }
            val frequencyTextField = rememberTextFieldState(Frequency.Daily.name)

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
                    Frequency.entries.forEach { frequency ->
                        DropdownMenuItem(
                            text = { Text(frequency.name, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                frequencyTextField.setTextAndPlaceCursorAtEnd(frequency.name)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            Text(text = "Urgency")
            var selectedIndex by remember { mutableIntStateOf(0) }
            val urgencyOptions = Urgency.entries
            SingleChoiceSegmentedButtonRow {
                urgencyOptions.forEachIndexed { index, urgency ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = urgencyOptions.size),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                    ) {
                        Text(urgency.name)
                    }
                }
            }

            Button(
                onClick = {
                    onContinue(
                        datePickerState.getSelectedDate(),
                        frequencyTextField.text.toString(),
                        urgencyOptions[selectedIndex],
                    )
                },
            ) {
                Text("Continue")
            }
        }
    }
}