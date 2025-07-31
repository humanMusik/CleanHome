package com.humanmusik.cleanhome.presentation.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks.Companion.invoke
import com.humanmusik.cleanhome.domain.repository.UpdateTask
import com.humanmusik.cleanhome.domain.repository.UpdateTask.Companion.invoke
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.asFlowState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    flowOfTasks: FlowOfTasks,
    private val taskEditor: TaskEditor,
    private val updateTask: UpdateTask,
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

    fun onEditTask(task: Task) { /* TODO: Edit task */
    }

    fun onCreateTask(task: Task) { /* TODO: Create task */}

    fun onCompleteTask(task: Task) {
        viewModelScope.launch {
            runCatching {
                taskEditor.reassignTask(
                    task = task,
                    dateCompleted = getTodayLocalDate(),
                )
            }
                .onSuccess { task -> updateTask(task) }
                .onFailure { /* TODO: Error - No Residents found */ }
        }
    }

    private fun getTodayLocalDate() = LocalDate.now()
}