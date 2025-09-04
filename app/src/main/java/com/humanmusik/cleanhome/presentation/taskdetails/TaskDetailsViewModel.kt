package com.humanmusik.cleanhome.presentation.taskdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.data.repository.FlowOfEnrichedTaskById
import com.humanmusik.cleanhome.data.repository.FlowOfEnrichedTaskById.Companion.invoke
import com.humanmusik.cleanhome.domain.enrichedTaskComparator
import com.humanmusik.cleanhome.navigation.TaskDetailsNavKey
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.asFlowState
import com.humanmusik.cleanhome.presentation.fromFlow
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.util.MutableSavedStateFlow
import com.humanmusik.cleanhome.util.doNotSaveState
import com.humanmusik.cleanhome.util.savedStateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel(assistedFactory = TaskDetailsViewModel.Factory::class)
class TaskDetailsViewModel @AssistedInject constructor(
    @Assisted private val navKey: TaskDetailsNavKey,
    flowOfEnrichedTaskById: FlowOfEnrichedTaskById,
) : ViewModel() {

    val state: MutableSavedStateFlow<FlowState<EnrichedTaskEntity>> = savedStateFlow(
        savedStateBehaviour = doNotSaveState(),
        initialState = FlowState.Loading(),
    )

    init {
        FlowState.fromFlow(
            flowOfEnrichedTaskById(taskId = navKey.taskId)
        )
            .onEach { flowState -> state.update { flowState } }
            .launchIn(viewModelScope)
    }

    @AssistedFactory
    interface Factory {
        fun create(navKey: TaskDetailsNavKey): TaskDetailsViewModel
    }
}