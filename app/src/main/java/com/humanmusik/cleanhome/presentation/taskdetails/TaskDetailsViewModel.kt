package com.humanmusik.cleanhome.presentation.taskdetails

import androidx.lifecycle.ViewModel
import com.humanmusik.cleanhome.navigation.TaskDetailsNavKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = TaskDetailsViewModel.Factory::class)
class TaskDetailsViewModel @AssistedInject constructor(
    @Assisted private val navKey: TaskDetailsNavKey,
) : ViewModel() {

    private var mutableStateFlow = MutableStateFlow(navKey.task)
    val stateFlow = mutableStateFlow.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(navKey: TaskDetailsNavKey): TaskDetailsViewModel
    }
}