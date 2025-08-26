package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.navigation.BackStackInstruction
import com.humanmusik.cleanhome.navigation.BackStackInstructor
import com.humanmusik.cleanhome.navigation.TaskCreationNavKey
import com.humanmusik.cleanhome.navigation.TaskListNavKey
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.isSuccess
import com.humanmusik.cleanhome.presentation.onLoading
import com.humanmusik.cleanhome.presentation.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import kotlin.time.Duration

@HiltViewModel(assistedFactory = TaskCreationDurationViewModel.Factory::class)
class TaskCreationDurationViewModel @AssistedInject constructor(
    @Assisted private val navKey: TaskCreationNavKey.Duration,
    private val taskEditor: TaskEditor,
    private val backStackInstructor: BackStackInstructor,
) : ViewModel() {
    val mutableStateFlow: MutableStateFlow<FlowState<Unit>> = MutableStateFlow(FlowState.Idle())
    val stateFlow = mutableStateFlow.asStateFlow()

    fun onCreateTask(duration: Duration): BackStackInstructor {
        val updatedTaskParcelData = navKey.taskParcelData.copy(duration = duration)

        FlowState.fromSuspendingFunc {
            taskEditor.assignTask(
                taskParcelData = updatedTaskParcelData,
                todayDate = getTodayLocalDate(),
            )
        }
            .onEach { mutableStateFlow.update { it } }
            .onSuccess {
                backStackInstructor.learnInstructions(
                    BackStackInstruction.PopUntil(TaskListNavKey)
                )
            }
            .launchIn(viewModelScope)

        return backStackInstructor
    }

    private fun getTodayLocalDate() = LocalDate.now()

    @AssistedFactory
    interface Factory {
        fun create(navKey: TaskCreationNavKey.Duration): TaskCreationDurationViewModel
    }
}