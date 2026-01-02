package com.humanmusik.cleanhome.presentation.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.data.mappers.toTask
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfEnrichedTasks
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfEnrichedTasks.Companion.invoke
import com.humanmusik.cleanhome.domain.EnrichedTaskFilter
import com.humanmusik.cleanhome.domain.TaskEditor
import com.humanmusik.cleanhome.domain.TaskEditorExceptions
import com.humanmusik.cleanhome.domain.model.task.State
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.asFlowState
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.onFailure
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.presentation.utils.composables.AlertDialogParams
import com.humanmusik.cleanhome.presentation.utils.composables.AlertDialogState
import com.humanmusik.cleanhome.util.MutableSavedStateFlow
import com.humanmusik.cleanhome.util.doNotSaveState
import com.humanmusik.cleanhome.util.savedStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val flowOfEnrichedTasks: FlowOfEnrichedTasks,
    private val taskEditor: TaskEditor,
) : ViewModel() {
    val state: MutableSavedStateFlow<TaskListState> = savedStateFlow(
        savedStateBehaviour = doNotSaveState(),
        initialState = TaskListState(
            enrichedTaskEntities = FlowState.Loading(),
            errorDialog = AlertDialogState.Hide,
            taskCompletionToast = TaskCompletionToastState.Hidden,
        ),
    )

    init {
        viewModelScope.launch {
            retrieveTasks()
        }
    }

    fun onCompleteTask(task: EnrichedTaskEntity) {
        lateinit var newTask: Task

        FlowState.fromSuspendingFunc {
            newTask = taskEditor.reassignTask(
                task = task.toTask(),
            )
        }
            .onEach { state.update { it.copy(taskToBeCompleted = task) } }
            .onSuccess {
                state.update {
                    it.copy(
                        taskCompletionToast = TaskCompletionToastState.Visible(
                            originalTaskId = task.id,
                            newTaskId = newTask.id
                                ?: throw IllegalStateException("Task id cannot be null"),
                        ),
                    )
                }
            }
            .onFailure { throwable ->
                when (throwable) {
                    is TaskEditorExceptions.AlreadyCompletedToday -> {
                        state.update {
                            it.copy(
                                errorDialog = AlertDialogState.Show(
                                    params = AlertDialogParams(
                                        key = TaskListDialogKeys.alreadyCompleted,
                                        dialogText = "Task has already been completed today",
                                        positiveButtonText = "OK",
                                        negativeButtonText = null,
                                    )
                                )
                            )
                        }
                    }

                    else -> {
                        state.update {
                            it.copy(
                                errorDialog = AlertDialogState.Show(
                                    params = AlertDialogParams(
                                        key = TaskListDialogKeys.taskCompletionError,
                                        dialogText = "Something went wrong when completing task.",
                                        positiveButtonText = "Try Again",
                                        negativeButtonText = "Cancel",
                                    )
                                )
                            )
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onTaskSelected(
        navigation: () -> Unit,
    ) {
        navigation()
    }

    fun retrieveTasks() {
        flowOfEnrichedTasks(filter = EnrichedTaskFilter.ByState(setOf(State.Active)))
            .asFlowState()
            .onEach { flowState ->
                state.update { it.copy(enrichedTaskEntities = flowState) }
            }
            .onFailure {
                state.update {
                    it.copy(
                        errorDialog = AlertDialogState.Show(
                            params = AlertDialogParams(
                                key = TaskListDialogKeys.errorLoadingTasks,
                                dialogText = "Something went wrong whilst loading your tasks.",
                                positiveButtonText = "Try Again",
                                negativeButtonText = "Cancel",
                            )
                        ),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun dismissErrorDialog() {
        state.update {
            it.copy(
                errorDialog = AlertDialogState.Hide,
                taskToBeCompleted = null,
            )
        }
    }

    fun onUndoTaskCompletion(
        originalTaskId: Task.Id,
        newTaskId: Task.Id,
    ) {
        viewModelScope.launch {
            taskEditor.undoTaskReassignment(
                originalTaskId = originalTaskId,
                newTaskId = newTaskId,
            )

            state.update {
                it.copy(
                    taskCompletionToast = TaskCompletionToastState.Hidden,
                    taskToBeCompleted = null,
                )
            }
        }
    }

    fun dismissTaskCompletionToast() {
        state.update {
            it.copy(
                taskCompletionToast = TaskCompletionToastState.Hidden,
                taskToBeCompleted = null,
            )
        }
    }
}