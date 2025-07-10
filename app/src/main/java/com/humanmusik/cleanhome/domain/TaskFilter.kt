package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.task.Task
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

sealed interface TaskFilter {
    data object All : TaskFilter
    data class ByAssignment(val assignedTo: Resident) : TaskFilter
    data class ByScheduledDate(
        val startDateInclusive: LocalDate,
        val endDateInclusive: LocalDate,
    ) : TaskFilter
}