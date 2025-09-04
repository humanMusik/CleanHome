package com.humanmusik.cleanhome.navigation

import android.os.Parcelable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskCreationParcelData
import kotlinx.parcelize.Parcelize

interface CustomNavKey : Parcelable

@Parcelize
data class BackStack(val navKeys: SnapshotStateList<CustomNavKey>) : Parcelable

@Parcelize
data object TaskListNavKey : CustomNavKey

@Parcelize
data class TaskDetailsNavKey(val taskId: Task.Id) : CustomNavKey

@Parcelize
sealed interface TaskCreationNavKey : CustomNavKey {
    data object NameRoom : TaskCreationNavKey

    data class DateFrequencyUrgency(
        val taskCreationParcelData: TaskCreationParcelData,
    ) : TaskCreationNavKey

    data class Duration(
        val taskCreationParcelData: TaskCreationParcelData,
    ) : TaskCreationNavKey
}