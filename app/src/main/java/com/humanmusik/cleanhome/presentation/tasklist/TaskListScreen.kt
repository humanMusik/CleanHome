package com.humanmusik.cleanhome.presentation.tasklist

import android.content.Context
import android.util.Log
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.onSuccess

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    onAddTaskNavigation: () -> Unit,
    onTaskSelectedNavigation: (Task.Id) -> Unit,
) {
    val state: FlowState<TaskListState> = viewModel.state.collectAsState().value
    TaskListContent(
        state = state,
        onComplete = viewModel::onCompleteTask,
        onTaskSelected = { taskId ->
            viewModel.onTaskSelected(navigation = { onTaskSelectedNavigation(taskId) })
        },
        onAddTask = onAddTaskNavigation,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskListContent(
    state: FlowState<TaskListState>,
    onComplete: (Context, EnrichedTaskEntity) -> Unit,
    onTaskSelected: (Task.Id) -> Unit,
    onAddTask: () -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
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
        state.onSuccess {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 12.dp),
            ) {
                items(
                    items = it.enrichedTaskEntities,
                ) { enrichedTask ->
                    SwipeToDismissItem(
                        onSwipeStartToEnd = { /* TODO */ },
                        onSwipeEndToStart = { onComplete(context, enrichedTask) },
                    ) {
                        TaskCard(
                            enrichedTask = enrichedTask,
                            onClick = {
                                onTaskSelected(enrichedTask.id)
                                Log.d("Les", "Task selected")
                            },
                        )
                    }
                }
            }
        }
    }
}