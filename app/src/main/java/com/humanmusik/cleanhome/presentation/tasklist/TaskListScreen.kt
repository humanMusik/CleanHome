package com.humanmusik.cleanhome.presentation.tasklist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.presentation.utils.composables.AlertDialog
import com.humanmusik.cleanhome.presentation.utils.composables.AlertDialogState

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    onAddTaskNavigation: () -> Unit,
    onTaskSelectedNavigation: (Task.Id) -> Unit,
) {
    val state: TaskListState by viewModel.state.collectAsState()

    TaskListContent(
        state = state,
        onComplete = viewModel::onCompleteTask,
        onTaskSelected = { taskId ->
            viewModel.onTaskSelected(navigation = { onTaskSelectedNavigation(taskId) })
        },
        onAddTask = onAddTaskNavigation,
        onRetryTaskLoad = viewModel::retrieveTasks,
        onUndoTask = viewModel::onUndoTaskCompletion,
        dismissTaskCompletionToast = viewModel::dismissTaskCompletionToast,
        dismissErrorDialog = viewModel::dismissErrorDialog,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskListContent(
    state: TaskListState,
    onComplete: (EnrichedTaskEntity) -> Unit,
    onTaskSelected: (Task.Id) -> Unit,
    onAddTask: () -> Unit,
    onRetryTaskLoad: () -> Unit,
    onUndoTask: (Task.Id, Task.Id) -> Unit,
    dismissTaskCompletionToast: () -> Unit,
    dismissErrorDialog: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val taskCompletionToast = state.taskCompletionToast
    LaunchedEffect(taskCompletionToast) {
        if (taskCompletionToast is TaskCompletionToastState.Visible) {
            val result = snackbarHostState.showSnackbar(
                message = "Task completed",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short,
            )

            if (result == SnackbarResult.ActionPerformed) {
                onUndoTask(taskCompletionToast.originalTaskId, taskCompletionToast.newTaskId)
            }

            dismissTaskCompletionToast()
        }
    }

    if (state.errorDialog is AlertDialogState.Show) {
        AlertDialog(
            params = state.errorDialog.params,
            onPositiveButtonPressed = {
                when (state.errorDialog.params.key) {
                    TaskListDialogKeys.alreadyCompleted -> {
                        dismissErrorDialog()
                    }

                    TaskListDialogKeys.taskCompletionError -> {
                        state.taskToBeCompleted?.let { onComplete(it) }
                    }

                    TaskListDialogKeys.errorLoadingTasks -> {
                        onRetryTaskLoad()
                    }
                }
            },
            onNegativeButtonPressed = { dismissErrorDialog() },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "CleanHome") },
                actions = {
                    IconButton(onClick = onAddTask) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Task")
                    }
                },
            )
        },
    ) { paddingValues ->
        state.enrichedTaskEntities.onSuccess {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 12.dp),
            ) {
                items(
                    items = it,
                ) { enrichedTask ->
                    SwipeToDismissItem(
                        onSwipeStartToEnd = { /* TODO */ },
                        onSwipeEndToStart = { onComplete(enrichedTask) },
                    ) {
                        TaskCard(
                            enrichedTask = enrichedTask,
                            onClick = { onTaskSelected(enrichedTask.id) },
                        )
                    }
                }
            }
        }
    }
}
