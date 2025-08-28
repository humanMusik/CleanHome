package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.lifecycle.ViewModel
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.domain.model.task.toFrequency
import com.humanmusik.cleanhome.navigation.BackStackInstruction
import com.humanmusik.cleanhome.navigation.BackStackInstructor
import com.humanmusik.cleanhome.navigation.TaskCreationNavKey
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate

@HiltViewModel(assistedFactory = TaskCreationDateFreqUrgencyViewModel.Factory::class)
class TaskCreationDateFreqUrgencyViewModel @AssistedInject constructor(
    @Assisted private val navKey: TaskCreationNavKey.DateFrequencyUrgency,
    private val backStackInstructor: BackStackInstructor,
) : ViewModel() {

    private val taskParcelData = navKey.taskParcelData

    fun onContinue(
        date: LocalDate?,
        frequency: String,
        urgency: Urgency,
    ): BackStackInstructor {
        val updatedTaskParcelData = taskParcelData.copy(
            date = date,
            frequency = frequency.toFrequency(),
            urgency = urgency,
        )

        return backStackInstructor
    }

    @AssistedFactory
    interface Factory {
        fun create(
            key: TaskCreationNavKey.DateFrequencyUrgency,
        ): TaskCreationDateFreqUrgencyViewModel
    }
}