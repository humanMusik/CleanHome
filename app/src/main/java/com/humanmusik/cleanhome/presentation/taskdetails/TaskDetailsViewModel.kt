package com.humanmusik.cleanhome.presentation.taskdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.data.repository.FlowOfAllRooms
import com.humanmusik.cleanhome.data.repository.FlowOfAllRooms.Companion.invoke
import com.humanmusik.cleanhome.data.repository.FlowOfEnrichedTaskById
import com.humanmusik.cleanhome.data.repository.FlowOfEnrichedTaskById.Companion.invoke
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.model.task.toFrequencyOrThrow
import com.humanmusik.cleanhome.domain.model.task.toUrgency
import com.humanmusik.cleanhome.navigation.TaskDetailsNavKey
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.fromFlow
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.getOrNull
import com.humanmusik.cleanhome.presentation.onFailure
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.util.MutableSavedStateFlow
import com.humanmusik.cleanhome.util.findOrThrow
import com.humanmusik.cleanhome.util.saveState
import com.humanmusik.cleanhome.util.savedStateFlow
import com.humanmusik.cleanhome.util.toEpochMillis
import com.humanmusik.cleanhome.util.toLocalDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

@HiltViewModel(assistedFactory = TaskDetailsViewModel.Factory::class)
class TaskDetailsViewModel @AssistedInject constructor(
    savedStateHandle: SavedStateHandle,
    @Assisted private val navKey: TaskDetailsNavKey,
    private val flowOfEnrichedTaskById: FlowOfEnrichedTaskById,
    private val flowOfAllRooms: FlowOfAllRooms,
    private val taskEditor: TaskEditor,
) : ViewModel() {

    val state: MutableSavedStateFlow<TaskDetailsState> = savedStateFlow(
        savedStateBehaviour = saveState(savedStateHandle),
        initialState = TaskDetailsState(
            editableFields = FlowState.Loading(),
            dialogState = DialogState.Hidden,
            rooms = FlowState.Loading(),
        ),
    )

    val flowOfEnrichedTask = flow {
        flowOfEnrichedTaskById(navKey.taskId)
            .distinctUntilChanged()
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)
            .collect(this@flow)
    }

    val flowOfRooms = flow {
        flowOfAllRooms()
            .distinctUntilChanged()
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)
            .collect(this@flow)
    }

    init {
        combine(
            FlowState.fromFlow(flowOfEnrichedTask),
            FlowState.fromFlow(flowOfRooms),
        ) { enrichedTask, rooms ->
            enrichedTask
                .onSuccess { enrichedTaskEntity ->
                    loadEditableFields(enrichedTaskEntity)
                }

            rooms
                .onSuccess { rooms ->
                    state.update { state ->
                        state.copy(
                            rooms = FlowState.Success(rooms)
                        )
                    }
                }
        }
            .launchIn(viewModelScope)
    }

    fun onFieldClicked(inputFieldKey: String) {
        state.update { state ->
            state.copy(
                dialogState = DialogState.Visible(
                    inputFieldKey = inputFieldKey,
                    title = dialogTitle(inputFieldKey),
                    initialValue = state.editableFields.getOrNull()
                        ?.findOrThrow { it.key.key == inputFieldKey }
                        ?.value
                        ?: ""
                )
            )
        }
    }

    fun onDialogDismissed() {
        hideDialog()
    }

    fun onDialogInputConfirmed(input: String, fieldKey: String) {
        editField(
            input = input,
            fieldKey = fieldKey,
        )

        hideDialog()
    }

    fun onUrgencyToggleClicked(isUrgent: Boolean) {
        editField(
            input = isUrgent.toString(),
            fieldKey = urgentFieldKey,
        )
    }

    fun onSave(navigation: () -> Unit) {
        viewModelScope.launch {
            state.value.editableFields.onSuccess { editableFields ->
                FlowState.fromSuspendingFunc {
                    taskEditor.editTask(
                        TaskEditParcelData(
                            id = navKey.taskId,
                            taskName = editableFields.findOrThrow { it.key == FieldKeys.taskNameField }.value,
                            roomId = flowOfRooms.first().findOrThrow { room ->
                                room.name == editableFields.findOrThrow { it.key == FieldKeys.roomField }.value
                            }
                                .id
                                ?: Room.Id(0),
                            scheduledDate = Instant.ofEpochMilli(
                                editableFields
                                    .findOrThrow { it.key == FieldKeys.dueDateField }
                                    .value
                                    .toLong()
                            ).toLocalDate(),
                            frequency = editableFields.findOrThrow { it.key == FieldKeys.frequencyField }
                                .value
                                .toFrequencyOrThrow(),
                            duration = Duration.parse(
                                editableFields.findOrThrow { it.key == FieldKeys.durationField }.value,
                            ),
                            urgency = editableFields.findOrThrow { it.key == FieldKeys.urgentField }
                                .value
                                .toBoolean()
                                .toUrgency(),
                            assigneeId = flowOfEnrichedTask.first().assigneeId,
                        )
                    )
                }
                    .onSuccess { navigation() }
                    .onFailure { /* TODO: Error dialog */ }
                    .launchIn(viewModelScope)
            }
        }
    }

    fun onDelete(navigation: () -> Unit) {
        FlowState.fromSuspendingFunc {
            taskEditor.deleteTask(navKey.taskId)
        }
            .onSuccess { navigation() }
            .onFailure { /* TODO: Error dialog */ }
            .launchIn(viewModelScope)
    }

    private fun loadEditableFields(enrichedTask: EnrichedTaskEntity) {
        state.update { state ->
            state.copy(
                editableFields = FlowState.Success(
                    buildList {
                        add(
                            EditableField(
                                key = FieldKeys.taskNameField,
                                title = "Task Name",
                                value = enrichedTask.taskName,
                                isEdited = false,
                            ),
                        )
                        add(
                            EditableField(
                                key = FieldKeys.roomField,
                                title = "Room",
                                value = enrichedTask.roomName,
                                isEdited = false,
                            ),
                        )
                        add(
                            EditableField(
                                key = FieldKeys.dueDateField,
                                title = "Due Date",
                                value = enrichedTask.scheduledDate.toEpochMillis().toString(),
                                isEdited = false,
                            ),
                        )
                        add(
                            EditableField(
                                key = FieldKeys.frequencyField,
                                title = "Frequency",
                                value = enrichedTask.frequency.name,
                                isEdited = false,
                            ),
                        )
                        add(
                            EditableField(
                                key = FieldKeys.durationField,
                                title = "Duration",
                                value = enrichedTask.duration.toString(),
                                isEdited = false,
                            ),
                        )
                        add(
                            EditableField(
                                key = FieldKeys.urgentField,
                                title = "Urgent",
                                value = enrichedTask.urgency.isUrgent().toString(),
                                isEdited = false,
                            ),
                        )
                    }
                        .sorted()
                )
            )
        }
    }

    private fun editField(
        input: String,
        fieldKey: String,
    ) {
        state.value.editableFields.onSuccess { fields ->
            state.update { state ->
                state.copy(
                    editableFields = FlowState.Success(
                        fields.map { field ->
                            if (field.key.key == fieldKey) {
                                field.copy(
                                    value = input,
                                    isEdited = true,
                                )
                            } else {
                                field
                            }
                        }
                    )
                )
            }
        }
    }

    private fun resetField(
        input: String,
        fieldKey: String,
    ) {
        state.value.editableFields.onSuccess { fields ->
            state.update { state ->
                state.copy(
                    editableFields = FlowState.Success(
                        fields.map { field ->
                            if (field.key.key == fieldKey) {
                                field.copy(
                                    value = input,
                                    isEdited = false,
                                )
                            } else {
                                field
                            }
                        }
                    )
                )
            }
        }
    }

    private fun hideDialog() {
        state.update {
            it.copy(
                dialogState = DialogState.Hidden,
            )
        }
    }

    private fun dialogTitle(fieldKey: String): String =
        when (fieldKey) {
            taskNameFieldKey -> "Task Name"
            roomFieldKey -> "Room"
            dueDateFieldKey -> "Task Due Date"
            frequencyFieldKey -> "Task Frequency"
            durationFieldKey -> "Task Duration"
            urgentFieldKey -> ""
            else -> ""
        }

    @AssistedFactory
    interface Factory {
        fun create(navKey: TaskDetailsNavKey): TaskDetailsViewModel
    }
}