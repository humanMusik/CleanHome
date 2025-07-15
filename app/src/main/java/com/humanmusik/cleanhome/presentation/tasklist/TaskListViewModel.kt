package com.humanmusik.cleanhome.presentation.tasklist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.repository.CleanHomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: CleanHomeRepository,
): ViewModel() {
    var state by mutableStateOf(TaskListState())

    fun onRefresh() {
        getAllTasks()
    }

    fun onEdit(task: Task) { /* TODO: Edit task */ }

    fun onComplete(task: Task) {
        // TODO
    }

    private fun getAllTasks() {
//        viewModelScope.launch {
//            repository
//                .getAllTasks()
//                .collect { result ->
//                    when (result) {
//                        is Resource.Loading -> {
//                            state = state.copy(isLoading = result.isLoading)
//                        }
//                        is Resource.Error -> Unit
//                        is Resource.Success -> result.data?.let {
//                            state = state.copy(
//                                tasks = it
//                            )
//                        }
//                    }
//                }
//        }
    }
}