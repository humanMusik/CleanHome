package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.navigation.TaskCreationNavKey
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.fromSuspendingFunc
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.util.MutableSavedStateFlow
import com.humanmusik.cleanhome.util.doNotSaveState
import com.humanmusik.cleanhome.util.savedStateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.Duration

@HiltViewModel(assistedFactory = TaskCreationDurationViewModel.Factory::class)
class TaskCreationDurationViewModel @AssistedInject constructor(
    @Assisted private val navKey: TaskCreationNavKey.Duration,
    private val taskEditor: TaskEditor,
) : ViewModel() {
    val state: MutableSavedStateFlow<FlowState<Unit>> = savedStateFlow(
        savedStateBehaviour = doNotSaveState(),
        initialState = FlowState.Idle()
    )

    fun onCreateTask(
        duration: Duration,
        navigation: () -> Unit,
    ) {
        val updatedTaskParcelData = navKey.taskCreationParcelData.copy(duration = duration)

        FlowState.fromSuspendingFunc {
            taskEditor.assignTask(
                taskCreationParcelData = updatedTaskParcelData,
                todayDate = getTodayLocalDate(),
            )
        }
            .onEach { state.update { it } }
            .onSuccess { navigation() }
            .launchIn(viewModelScope)
    }

    private fun getTodayLocalDate() = LocalDate.now()

    @AssistedFactory
    interface Factory {
        fun create(navKey: TaskCreationNavKey.Duration): TaskCreationDurationViewModel
    }
}