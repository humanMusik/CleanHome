package com.humanmusik.cleanhome.presentation.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.data.repository.FlowOfTasks
import com.humanmusik.cleanhome.data.repository.FlowOfTasks.Companion.invoke
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.navigation.BackStackInstruction
import com.humanmusik.cleanhome.navigation.BackStackInstructor
import com.humanmusik.cleanhome.navigation.NavigationViewModel
import com.humanmusik.cleanhome.navigation.TaskCreationNavKey
import com.humanmusik.cleanhome.navigation.TaskDetailsNavKey
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.asFlowState
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.onFailure
import com.humanmusik.cleanhome.presentation.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    flowOfTasks: FlowOfTasks,
    private val taskEditor: TaskEditor,
    private val backStackInstructor: BackStackInstructor,
) : ViewModel() {
    private var mutableStateFlow: MutableStateFlow<FlowState<TaskListModel>> =
        MutableStateFlow(FlowState.Loading())
    val stateFlow = mutableStateFlow.asStateFlow()

    init {
        flowOfTasks(filter = TaskFilter.All)
            .asFlowState()
            .onSuccess { tasks ->
                mutableStateFlow.update {
                    FlowState.Success(
                        TaskListModel(tasks = tasks.toList()),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEditTask(task: Task) {
        viewModelScope.launch {
            // TODO: Go to task edit screen
        }
    }

    fun onCompleteTask(task: Task) {
        // Optimistically remove completed task
        mutableStateFlow.value.onSuccess {
            val updatedTasks = it.tasks.toMutableList().apply { this.remove(task) }.toList()

            mutableStateFlow.update {
                FlowState.Success(TaskListModel(tasks = updatedTasks))
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