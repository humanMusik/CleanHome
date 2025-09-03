package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.Urgency
import kotlin.Comparator

fun Urgency.sortOrder(): Int = when (this) {
    Urgency.Urgent -> 0
    Urgency.NonUrgent -> 1
}

internal val taskComparator: Comparator<Task> = compareBy(
    { it.scheduledDate },
    { it.urgency?.sortOrder() },
    { it.duration },
)

internal val enrichedTaskComparator: Comparator<EnrichedTaskEntity> = compareBy(
    { it.scheduledDate },
    { it.urgency.sortOrder() },
    { it.duration },
)