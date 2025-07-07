package com.humanmusik.cleanhome.presentation.tasklist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TaskListScreen(
//    navigator: DestinationsNavigator,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val swipeRefreshState = rememberPullToRefreshState()

//    PullToRefreshBox(
//        isRefreshing = state.isRefreshing,
//        onRefresh = viewModel::onRefresh, // TODO: need to update state.isRefreshing
//        state = swipeRefreshState,
//    ) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Email App") },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(
                items = state.tasks,
            ) { task ->
                TaskItem(
                    task = task,
                    onEdit = { viewModel.onEdit(task) },
                    onComplete = { viewModel.onComplete(task) },
                )
            }

        }
    }

//    }
}