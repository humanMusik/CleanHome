package com.humanmusik.cleanhome.presentation.tasklist

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.onSuccess

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    onAddTaskNavigator: () -> Unit,
    onExamineNavigator: (Task) -> Unit,
) {
    Log.d("TaskListViewModel", viewModel.hashCode().toString())
    val state: FlowState<TaskListState> = viewModel.state.collectAsState().value
    TaskListContent(
        state = state,
        onEdit = viewModel::onEditTask,
        onComplete = viewModel::onCompleteTask,
        onExamine = onExamineNavigator,
        onAddTask = onAddTaskNavigator,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskListContent(
    state: FlowState<TaskListState>,
    onEdit: (Task) -> Unit,
    onComplete: (Task) -> Unit,
    onExamine: (Task) -> Unit,
    onAddTask: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "CleanHome") },
                navigationIcon = {
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
                    items = it.tasks,
                ) { task ->
                    TaskItem(
                        task = task,
                        onSwipeStartToEnd = { providedTask -> onEdit(providedTask) },
                        onSwipeEndToStart = { providedTask -> onComplete(providedTask) },
                        onClick = { onExamine(task) },
                    )
                }
            }
        }
    }
}