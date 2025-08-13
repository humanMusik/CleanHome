package com.humanmusik.cleanhome.presentation.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks.Companion.invoke
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.asFlowState
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.map
import com.humanmusik.cleanhome.presentation.onFailure
import com.humanmusik.cleanhome.presentation.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    flowOfTasks: FlowOfTasks,
    private val taskEditor: TaskEditor,
) : ViewModel() {
    private var _stateFlow: MutableStateFlow<TaskListModel> = MutableStateFlow(
        TaskListModel(tasks = FlowState.Loading()),
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        flowOfTasks(filter = TaskFilter.All)
            .asFlowState()
            .onEach { flowState ->
                _stateFlow.value = TaskListModel(
                    tasks = flowState.map { it.toList() },
                )
            }
            .launchIn(viewModelScope)
    }

    fun onEditTask(task: Task) {
        viewModelScope.launch {
            // TODO: Go to task edit screen
        }
    }

    fun onAddTask(task: Task) {
        viewModelScope.launch {
            // TODO: Go to task creation screen
        }
    }

    fun onCompleteTask(task: Task) {
        // Optimistically remove completed task
        _stateFlow.value.tasks.onSuccess {
            val updatedTasks = it.toMutableList().apply { this.remove(task) }.toList()

            _stateFlow.update { state ->
                state.copy(
                    tasks = FlowState.Success(updatedTasks)
                )
            }
        }

        FlowState.fromSuspendingFunc {
            taskEditor.reassignTask(
                task = task,
                dateCompleted = getTodayLocalDate(),
            )
        }
            .onFailure {
                // TODO: NoResidentsFound() - No Residents found
                // TODO: ServerError() - Dialog
                // TODO: OtherError() - Dialog
            }
            .launchIn(viewModelScope)
    }

    private fun getTodayLocalDate() = LocalDate.now()
}