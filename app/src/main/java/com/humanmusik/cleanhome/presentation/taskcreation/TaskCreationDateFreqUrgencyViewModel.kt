package com.humanmusik.cleanhome.presentation.taskcreation

import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.navigation.TaskCreationNavKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate

@HiltViewModel(assistedFactory = TaskCreationDateFreqUrgencyViewModel.Factory::class)
class TaskCreationDateFreqUrgencyViewModel @AssistedInject constructor(
    @Assisted private val navKey: TaskCreationNavKey.DateFrequencyUrgency,
) {

    fun onContinue(
        date: LocalDate,
        frequency: Frequency,
        urgency: Urgency,
    ) {
        navKey.taskParcelData.copy(
            date = date,
            frequency = frequency,
            urgency = urgency,
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(
            key: TaskCreationNavKey.DateFrequencyUrgency,
        ): TaskCreationDateFreqUrgencyViewModel
    }
}