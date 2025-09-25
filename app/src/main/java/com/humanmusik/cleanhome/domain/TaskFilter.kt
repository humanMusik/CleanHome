package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.task.State
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
    data class ByState(val states: Set<State>) : TaskFilter

    class AllOf(val filters: Set<TaskFilter>) : TaskFilter {
        init {
            require(filters.size >= 2)
        }

        override val all: Set<TaskFilter>
            get() = this.filters
    }

    infix fun and(moreFilters: TaskFilter): TaskFilter {
        return AllOf(this.all + moreFilters.all)
    }

    val all: Set<TaskFilter>
        get() = setOf(this)
}

sealed interface EnrichedTaskFilter {
    data object All : EnrichedTaskFilter
    data class ById(val ids: Set<Task.Id>) : EnrichedTaskFilter
    data class ByAssignment(val residents: Set<Resident.Id>) : EnrichedTaskFilter
    data class ByScheduledDate(
        val startDateInclusive: LocalDate,
        val endDateInclusive: LocalDate,
    ) : EnrichedTaskFilter
    data class ByState(val states: Set<State>) : EnrichedTaskFilter
}