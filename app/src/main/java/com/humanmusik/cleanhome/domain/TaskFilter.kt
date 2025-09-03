package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.task.Task
import java.time.LocalDate

sealed interface TaskFilter {
    data object All : TaskFilter
    data class ById(val ids: Set<Task.Id>) : TaskFilter
    data class ByAssignment(val residents: Set<Resident.Id>) : TaskFilter
    data class ByScheduledDate(
        val startDateInclusive: LocalDate,
        val endDateInclusive: LocalDate,
    ) : TaskFilter
}

sealed interface EnrichedTaskFilter {
    data object All : EnrichedTaskFilter
    data class ById(val ids: Set<Task.Id>) : EnrichedTaskFilter
    data class ByAssignment(val residents: Set<Resident.Id>) : EnrichedTaskFilter
    data class ByScheduledDate(
        val startDateInclusive: LocalDate,
        val endDateInclusive: LocalDate,
    ) : EnrichedTaskFilter
}