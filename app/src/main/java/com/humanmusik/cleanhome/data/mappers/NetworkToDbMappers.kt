package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.api.task.Task
import com.humanmusik.cleanhome.data.entities.TaskEntity
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.State
import com.humanmusik.cleanhome.domain.model.task.toUrgency
import com.humanmusik.cleanhome.util.toLocalDate
import java.time.Duration
import java.time.Instant

fun Task.toTaskEntity() = TaskEntity(
    id = id ?: throw IllegalStateException(),
    name = name ?: throw IllegalStateException(),
    roomId = roomId?.toInt() ?: throw IllegalStateException(),
    duration = duration?.let { Duration.parse(it) } ?: throw IllegalStateException(),
    frequency = frequency?.let { Frequency.fromString(it) } ?: throw IllegalStateException(),
    scheduledDate = scheduledDate?.let { Instant.ofEpochMilli(it.toLong()).toLocalDate() }
        ?: throw IllegalStateException(),
    urgency = urgent?.toUrgency() ?: throw IllegalStateException(),
    assigneeId = assigneeId?.toInt() ?: throw IllegalStateException(),
    state = state?.let { State.fromString(it) } ?: throw IllegalStateException(),
)

fun List<Task>.toTaskEntities() = map { it.toTaskEntity() }
