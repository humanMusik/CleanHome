package com.humanmusik.cleanhome.presentation.tasklist

import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.utils.composables.AlertDialogState

data class TaskListState(
    val enrichedTaskEntities: FlowState<List<EnrichedTaskEntity>>,
    val errorDialog: AlertDialogState,
    val taskToBeCompleted: EnrichedTaskEntity? = null,
    val taskCompletionToast: TaskCompletionToastState,
)

sealed interface TaskCompletionToastState {
    data object Hidden : TaskCompletionToastState
    data class Visible(
        val originalTaskId: Task.Id,
        val newTaskId: Task.Id,
    ) : TaskCompletionToastState
}

internal object TaskListDialogKeys {
    val alreadyCompleted = "already-completed"
    val taskCompletionError = "completion-error"
    val errorLoadingTasks = "error-loading-tasks"
}