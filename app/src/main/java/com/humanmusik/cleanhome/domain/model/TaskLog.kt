package com.humanmusik.cleanhome.domain.model

import java.time.LocalDate

data class TaskLog(
    val id: Int? = null,
    val taskId: Int,
    val date: LocalDate,
    val recordedAction: ActionType,
) {
    fun wasCompletedOn(date: LocalDate): Boolean =
        this.date == date && recordedAction == ActionType.Complete
}

enum class ActionType {
    Complete,
    Create,
    Delete,
    Edit,
}