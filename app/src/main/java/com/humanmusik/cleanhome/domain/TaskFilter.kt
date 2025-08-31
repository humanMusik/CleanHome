package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.ResidentId
import com.humanmusik.cleanhome.domain.model.task.TaskId
import java.time.LocalDate

sealed interface TaskFilter {
    data object All : TaskFilter
    data class ById(val ids: Set<TaskId>) : TaskFilter
    data class ByAssignment(val residents: Set<ResidentId>) : TaskFilter
    data class ByScheduledDate(
        val startDateInclusive: LocalDate,
        val endDateInclusive: LocalDate,
    ) : TaskFilter
}