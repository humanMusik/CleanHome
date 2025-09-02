package com.humanmusik.cleanhome.presentation.tasklist

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.data.mappers.toTask
import com.humanmusik.cleanhome.data.repository.CreateTaskLog
import com.humanmusik.cleanhome.data.repository.CreateTaskLog.Companion.invoke
import com.humanmusik.cleanhome.data.repository.FlowOfEnrichedTasks
import com.humanmusik.cleanhome.data.repository.FlowOfEnrichedTasks.Companion.invoke
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.ActionType
import com.humanmusik.cleanhome.domain.model.TaskLog
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.model.task.TaskEditorExceptions
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.asFlowState
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.onFailure
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.util.MutableSavedStateFlow
import com.humanmusik.cleanhome.util.doNotSaveState
import com.humanmusik.cleanhome.util.savedStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    flowOfEnrichedTasks: FlowOfEnrichedTasks,
    private val taskEditor: TaskEditor,
    private val createTaskLog: CreateTaskLog,
) : ViewModel() {
    val state: MutableSavedStateFlow<FlowState<TaskListState>> = savedStateFlow(
        savedStateBehaviour = doNotSaveState(),
        initialState = FlowState.Loading(),
    )

    init {
        flowOfEnrichedTasks(filter = TaskFilter.All)
            .asFlowState()
            .onSuccess { enrichedTask ->
                state.update {
                    FlowState.Success(
                        TaskListState(enrichedTaskEntities = enrichedTask.toList()),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onCompleteTask(
        context: Context,
        enrichedTask: EnrichedTaskEntity,
    ) {
        // Optimistically remove completed task
        state.value.onSuccess {
            val updatedTasks = it.enrichedTaskEntities.toMutableList().apply { this.remove(enrichedTask) }.toList()

            state.update {
                FlowState.Success(TaskListState(enrichedTaskEntities = updatedTasks))
            }
        }

        FlowState.fromSuspendingFunc {
            taskEditor.reassignTask(
                task = enrichedTask.toTask(),
                dateCompleted = getTodayLocalDate(),
            )
        }
            .onSuccess {
                Toast.makeText(context, "Task Completed", Toast.LENGTH_SHORT).show()
                FlowState.fromSuspendingFunc {
                    createTaskLog(
                        taskLog = TaskLog(
                            id = null,
                            taskId = enrichedTask.id,
                            date = getTodayLocalDate(),
                            recordedAction = ActionType.Complete,
                        )
                    )
                }
                    .launchIn(viewModelScope)
            }
            .onFailure { throwable ->
                when (throwable) {
                    is TaskEditorExceptions.AlreadyCompletedToday -> {
                        state.value.onSuccess {
                            val updatedTasks = it.enrichedTaskEntities.toMutableList().apply { this.add(enrichedTask) }.toList()

                            state.update {
                                FlowState.Success(TaskListState(enrichedTaskEntities = updatedTasks))
                            }
                        }
                        Toast.makeText(context, "Task has already been completed today!", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
                // TODO: NoResidentsFound() - No Residents found
                // TODO: ServerError() - Dialog
                // TODO: OtherError() - Dialog
            }
            .launchIn(viewModelScope)
    }

    private fun getTodayLocalDate() = LocalDate.now()
}