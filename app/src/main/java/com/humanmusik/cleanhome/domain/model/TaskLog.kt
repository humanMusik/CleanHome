package com.humanmusik.cleanhome.domain.model

import android.os.Parcelable
import com.humanmusik.cleanhome.domain.model.task.Task
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class TaskLog(
    val id: Id?,
    val taskId: Task.Id,
    val date: LocalDate,
    val recordedAction: ActionType,
) : Parcelable {

    @JvmInline
    @Parcelize
    value class Id(val value: Int) : Parcelable

    fun wasCompletedOn(date: LocalDate): Boolean =
        this.date == date && recordedAction == ActionType.Complete
}

enum class ActionType {
    Complete,
    Create,
    Delete,
    Edit,
}