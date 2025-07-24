package com.humanmusik.cleanhome.presentation.tasklist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks.Companion.invoke
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val flowOfTasks: FlowOfTasks,
    private val flowOfAllResidents: FlowOfAllResidents,
    private val taskEditor: TaskEditor,
) : ViewModel() {
    var state by mutableStateOf(TaskListState())

    fun onRefresh() {
        flowOfTasks(filter = TaskFilter.All)
        // Create a loadingState
    }

    fun onEdit(task: Task) { /* TODO: Edit task */
    }

    fun onComplete(task: Task) {
        taskEditor.reassignTask(
            task = task,
            dateCompleted = /* TODO: dateComplete */
        )
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