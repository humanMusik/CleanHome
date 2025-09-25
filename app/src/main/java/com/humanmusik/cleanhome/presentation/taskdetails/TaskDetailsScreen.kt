package com.humanmusik.cleanhome.presentation.taskdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.selectAll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.cleanhome.R
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.getOrNull
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.util.fromHex
import com.humanmusik.cleanhome.util.toDateString
import com.humanmusik.cleanhome.util.toEpochMillis
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.format.FormatStyle

@Composable
fun TaskDetailsScreen(
    viewModel: TaskDetailsViewModel,
    onSaveNavigation: () -> Unit,
    onDeleteNavigation: () -> Unit,
) {
    val state = viewModel.state.collectAsState().value

    TaskDetailsContent(
        state = state,
        onFieldClicked = viewModel::onFieldClicked,
        onDialogDismissed = viewModel::onDialogDismissed,
        onDialogInputConfirmed = viewModel::onDialogInputConfirmed,
        onUrgencyToggleClicked = viewModel::onUrgencyToggleClicked,
        onSave = { viewModel.onSave(onSaveNavigation) },
        onDelete = { viewModel.onDelete(onDeleteNavigation) }
    )
}

@Composable
private fun TaskDetailsContent(
    state: TaskDetailsState,
    onFieldClicked: (String) -> Unit,
    onUrgencyToggleClicked: (Boolean) -> Unit,
    onDialogDismissed: () -> Unit,
    onDialogInputConfirmed: (String, String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {
    Scaffold { scaffoldPadding ->
        state.editableFields.onSuccess { editableFields ->
            val taskNameField = editableFields.find { it.key == FieldKeys.taskNameField }
            val roomField = editableFields.find { it.key == FieldKeys.roomField }
            val dueDateField = editableFields.find { it.key == FieldKeys.dueDateField }
            val frequencyField = editableFields.find { it.key == FieldKeys.frequencyField }
            val durationField = editableFields.find { it.key == FieldKeys.durationField }
            val urgencyField = editableFields.find { it.key == FieldKeys.urgentField }

            Column(
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    EditCell(
                        title = taskNameField?.title ?: "",
                        value = taskNameField?.value ?: "",
                        onClick = { onFieldClicked(taskNameFieldKey) },
                    )

                    EditCell(
                        title = roomField?.title ?: "",
                        value = roomField?.value ?: "",
                        onClick = { onFieldClicked(roomFieldKey) },
                    )

                    EditCell(
                        title = dueDateField?.title ?: "",
                        value = dueDateField?.value?.let {
                            Instant.ofEpochMilli(it.toLong())
                                .toDateString(
                                    FormatStyle.LONG
                                )
                        } ?: "",
                        onClick = { onFieldClicked(dueDateFieldKey) }
                    )

                    EditCell(
                        title = frequencyField?.title ?: "",
                        value = frequencyField?.value ?: "",
                        onClick = { onFieldClicked(frequencyFieldKey) },
                    )

                    EditCell(
                        title = durationField?.title ?: "",
                        value = durationField?.value?.let {
                            Duration.parse(it).toDurationString()
                        } ?: "",
                        onClick = { onFieldClicked(durationFieldKey) }
                    )

                    ToggleableCell(
                        title = urgencyField?.title ?: "",
                        isUrgent = urgencyField?.value.toBoolean(),
                        onClick = { isToggled -> onUrgencyToggleClicked(isToggled) }
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DeleteTaskButton(onClick = onDelete)
                    SaveTaskButton(onClick = onSave)
                }
            }
        }

        if (state.dialogState.isVisible()) {
            val dialog = state.dialogState

            Dialog(
                inputFieldKey = dialog.inputFieldKey,
                title = dialog.title,
                initialValue = dialog.initialValue,
                listOfRooms = state.rooms.getOrNull()?.map { it.name } ?: emptyList(),
                listOfFrequencies = Frequency.entries.map { it.name },
                onDismissRequest = onDialogDismissed,
                onConfirm = { input -> onDialogInputConfirmed(input, dialog.inputFieldKey) },
            )
        }
    }

}

@Composable
private fun EditCell(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onClick: () -> Unit = {},
) {
    Cell(
        modifier = modifier.clickable { onClick() },
        leadingContent = {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        },
        trailingContent = {
            Box(modifier.padding(end = 8.dp)) {
                Icon(
                    modifier = modifier
                        .size(DpSize(width = 64.dp, height = 64.dp))
                        .padding(PaddingValues(16.dp)),
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Task",
                )
            }
        },
    )
}

@Composable
private fun ToggleableCell(
    modifier: Modifier = Modifier,
    title: String,
    isUrgent: Boolean,
    onClick: (Boolean) -> Unit,
) {
    var isChecked by remember { mutableStateOf(isUrgent) }

    Cell(
        modifier = modifier.clickable {
            isChecked = !isChecked
            onClick(isChecked)
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(start = 16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }
        },
        trailingContent = {
            Switch(
                modifier = Modifier.padding(end = 16.dp),
                checked = isChecked,
                onCheckedChange = { newValue ->
                    isChecked = newValue
                    onClick(isChecked)
                },
            )
        },
    )

}

@Composable
private fun Cell(
    modifier: Modifier = Modifier,
    leadingContent: @Composable RowScope.() -> Unit,
    trailingContent: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically,
    ) {
        leadingContent()
        trailingContent()
    }
}

@Composable
private fun Dialog(
    inputFieldKey: String,
    title: String,
    initialValue: String,
    listOfRooms: List<String>,
    listOfFrequencies: List<String>,
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    when (inputFieldKey) {
        taskNameFieldKey,
            -> TextInputDialog(
            title = title,
            initialText = initialValue,
            onDismissRequest = onDismissRequest,
            onConfirm = onConfirm,
        )

        roomFieldKey,
            -> ListPickerDialog(
            onDismissRequest = onDismissRequest,
            title = title,
            initialValue = initialValue,
            items = listOfRooms,
            onConfirm = onConfirm,
        )

        dueDateFieldKey,
            -> DateInputDialog(
            onDismissRequest = onDismissRequest,
            onConfirm = { selectedDateMillis ->
                onConfirm(selectedDateMillis.toString())
            },
        )

        frequencyFieldKey,
            -> ListPickerDialog(
            onDismissRequest = onDismissRequest,
            title = title,
            initialValue = initialValue,
            items = listOfFrequencies,
            onConfirm = onConfirm,
        )

        durationFieldKey,
            -> DurationInputDialog(
            onDismissRequest = onDismissRequest,
            initialDuration = Duration.parse(initialValue),
            onConfirm = { duration ->
                onConfirm(duration.toString())
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextInputDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    title: String,
    initialText: String = "",
    label: String = "Enter text",
) {
    val textFieldState = rememberTextFieldState(initialText)

    LaunchedEffect(Unit) {
        textFieldState.edit { selectAll() }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = {
            Column {
                TextField(
                    state = textFieldState,
                    label = { Text(label) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(textFieldState.text.toString()) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun DateInputDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (Long) -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = {
                // Handle selected date from datePickerState.selectedDateMillis
                datePickerState.selectedDateMillis?.let { onConfirm(it) }
            }) {
                Text("Confirm")
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun ListPickerDialog(
    onDismissRequest: () -> Unit,
    title: String,
    initialValue: String,
    items: List<String>,
    onConfirm: (String) -> Unit,
) {
    var selectedTask by remember { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = {
            LazyColumn {
                items(items) { item ->
                    ListItem(
                        modifier = Modifier.clickable {
                            selectedTask = item
                        },
                        colors = if (item == selectedTask) {
                            ListItemDefaults.colors(containerColor = Color.fromHex("8DA9E3"))
                        } else {
                            ListItemDefaults.colors()
                        },
                        headlineContent = { Text(item) },
                        trailingContent = {
                            if (item == selectedTask) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Edit Task",
                                )
                            }
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedTask) }) {
                Text("Confirm")
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DurationInputDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (Duration) -> Unit,
    initialDuration: Duration,
) {
    val timePickerState =
        rememberTimePickerState(
            initialHour = initialDuration.toHours().toInt(),
            initialMinute = initialDuration.toMinutesPart(),
            is24Hour = true,
        )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Duration") },
        text = {
            Column(modifier = Modifier.padding(16.dp)) {
                TimeInput(state = timePickerState)
            }
        },
        confirmButton = {
            Button(onClick = {
                val duration = Duration.ofHours(timePickerState.hour.toLong())
                    .plusMinutes(timePickerState.minute.toLong())
                onConfirm(duration)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ColumnScope.DeleteTaskButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .widthIn(min = 164.dp)
            .padding(8.dp)
            .align(Alignment.CenterHorizontally),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.fromHex("D10D27"),
        ),
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "DELETE",
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Visible,
        )
    }
}

@Composable
private fun ColumnScope.SaveTaskButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .widthIn(min = 164.dp)
            .padding(8.dp)
            .align(Alignment.CenterHorizontally),
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "SAVE",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Visible,
        )
    }
}

@Composable
private fun Duration.toDurationString(): String {
    val hours = toHours()
    val hoursSuffix = pluralStringResource(
        id = R.plurals.hours_suffix,
        count = hours.toInt(),
    )
    val minutes = toMinutesPart()
    val minutesSuffix = pluralStringResource(
        id = R.plurals.minutes_suffix,
        count = minutes,
    )

    return buildString {
        if (hours > 0) {
            append(hours)
            append(hoursSuffix)
        }

        if (hours > 0 && minutes > 0) append(" and ")

        if (minutes > 0) {
            append(minutes)
            append(minutesSuffix)
        }
    }
}

@Preview
@Composable
fun Preview_EditCell() {
    Box {
        TaskDetailsContent(
            state = TaskDetailsState(
                editableFields = FlowState.Success(
                    listOf(
                        EditableField(
                            key = FieldKeys.taskNameField,
                            title = "Task Name",
                            value = "Clean toilet",
                            isEdited = false,
                        ),
                        EditableField(
                            key = FieldKeys.roomField,
                            title = "Room",
                            value = "Bathroom",
                            isEdited = false,
                        ),
                        EditableField(
                            key = FieldKeys.frequencyField,
                            title = "Frequency",
                            value = "Weekly",
                            isEdited = false,
                        ),
                        EditableField(
                            key = FieldKeys.dueDateField,
                            title = "Due Date",
                            value = LocalDate.of(2025, 9, 21).toEpochMillis().toString(),
                            isEdited = false,
                        ),
                        EditableField(
                            key = FieldKeys.durationField,
                            title = "Duration",
                            value = Duration.ofHours(1).plusMinutes(10).toString(),
                            isEdited = false,
                        ),
                        EditableField(
                            key = FieldKeys.urgentField,
                            title = "Urgent",
                            value = true.toString(),
                            isEdited = false,
                        ),
                    ),
                ),
                dialogState = DialogState.Hidden,
                rooms = FlowState.Success(emptyList()),
            ),
            onFieldClicked = {},
            onUrgencyToggleClicked = {},
            onDialogDismissed = {},
            onDialogInputConfirmed = {_, _ -> },
            onSave = {},
            onDelete = {},
        )
    }
}
