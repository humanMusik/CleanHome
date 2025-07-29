package com.humanmusik.cleanhome.presentation.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks.Companion.invoke
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.asFlowState
import com.humanmusik.cleanhome.presentation.fromFlow
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.getOrNull
import com.humanmusik.cleanhome.presentation.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val flowOfTasks: FlowOfTasks,
    private val flowOfAllResidents: FlowOfAllResidents,
    private val taskEditor: TaskEditor,
) : ViewModel() {
    private var _stateFlow: MutableStateFlow<TaskListState> = MutableStateFlow(
        TaskListState(tasks = FlowState.Loading()),
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        flowOfTasks(filter = TaskFilter.All)
            .asFlowState()
            .onEach { flowState ->
                _stateFlow.value = TaskListState(
                    tasks = flowState,
                )
            }
    }
    
    fun onRefresh() {
        flowOfTasks(filter = TaskFilter.All)
        // Create a loadingState
    }

    fun onEdit(task: Task) { /* TODO: Edit task */
    }

    fun onComplete(task: Task) {
        FlowState
            .fromFlow(
                taskEditor.reassignTask(
                    task = task,
                    dateCompleted = getTodayLocalDate(),
                )
            )
            .onSuccess { /* TODO: Call UpdateTask */ }
            .launchIn(viewModelScope)
    }

    private fun getTodayLocalDate() = LocalDate.now()

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