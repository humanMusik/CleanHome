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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.presentation.utils.composables.AlertDialog
import com.humanmusik.cleanhome.presentation.utils.composables.AlertDialogState
import java.time.Duration
import java.time.LocalDate

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
                title = {
                    Text(text = "Tasks")
                },
                actions = {
                    IconButton(onClick = onAddTask) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Task")
                    }
                },
            )
        },
    ) { scaffoldPadding ->
        state.enrichedTaskEntities.onSuccess {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
            ) {
                items(
                    items = it,
                ) { enrichedTask ->
                    val cardPadding = PaddingValues(vertical = 4.dp)

                    SwipeToDismissItem(
                        modifier = Modifier.padding(cardPadding),
                        onSwipeEndToStart = { onComplete(enrichedTask) },
                    ) {
                        TaskCard(
                            modifier = Modifier.padding(cardPadding),
                            enrichedTask = enrichedTask,
                            onClick = { onTaskSelected(enrichedTask.id) },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_TaskListContent() {
    val mockTasks = listOf(
        EnrichedTaskEntity(
            idValue = "task-1",
            taskName = "Vacuum Living Room",
            duration = Duration.ofMinutes(30),
            frequency = Frequency.Weekly,
            scheduledDate = LocalDate.now(),
            urgency = Urgency.Urgent,
            roomIdValue = "room-1",
            roomName = "Living Room",
            assigneeIdValue = "user-1",
            assigneeName = "Leslie",
            state = com.humanmusik.cleanhome.domain.model.task.State.Active,
            lastCompletedDate = null
        ),
        EnrichedTaskEntity(
            idValue = "task-2",
            taskName = "Wipe Kitchen Counters",
            duration = Duration.ofMinutes(10),
            frequency = Frequency.Daily,
            scheduledDate = LocalDate.now().plusDays(1),
            urgency = Urgency.Urgent,
            roomIdValue = "room-2",
            roomName = "Kitchen",
            assigneeIdValue = "user-2",
            assigneeName = "John",
            state = com.humanmusik.cleanhome.domain.model.task.State.Active,
            lastCompletedDate = LocalDate.now().minusDays(1)
        ),
        EnrichedTaskEntity(
            idValue = "task-3",
            taskName = "Clean Bathroom Mirror",
            duration = Duration.ofMinutes(5),
            frequency = Frequency.Fortnightly,
            scheduledDate = LocalDate.now().minusDays(3),
            urgency = Urgency.NonUrgent,
            roomIdValue = "room-3",
            roomName = "Bathroom",
            assigneeIdValue = "user-1",
            assigneeName = "Leslie",
            state = com.humanmusik.cleanhome.domain.model.task.State.Active,
            lastCompletedDate = null
        )
    )

    val mockState = TaskListState(
        enrichedTaskEntities = FlowState.Success(mockTasks),
        errorDialog = AlertDialogState.Hide,
        taskCompletionToast = TaskCompletionToastState.Hidden,
        taskToBeCompleted = null
    )

    TaskListContent(
        state = mockState,
        onComplete = {},
        onTaskSelected = {},
        onAddTask = {},
        onRetryTaskLoad = {},
        onUndoTask = { _, _ -> },
        dismissTaskCompletionToast = {},
        dismissErrorDialog = {}
    )
}

