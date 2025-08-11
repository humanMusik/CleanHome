package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.domain.model.Resident
import java.time.LocalDate

sealed interface TaskFilter {
    data object All : TaskFilter
    data class ById(val ids: Set<Int>) : TaskFilter
    data class ByAssignment(val residents: Set<Resident>) : TaskFilter
    data class ByScheduledDate(
        val startDateInclusive: LocalDate,
        val endDateInclusive: LocalDate,
    ) : TaskFilter
}