package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.data.entities.TaskEntity
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Task

fun TaskEntity.toTask() = Task(
    id = Task.Id(id),
    name = name,
    roomId = Room.Id(roomId),
    duration = duration,
    frequency = frequency,
    scheduledDate = scheduledDate,
    urgency = urgency,
    assigneeId = assigneeId?.let { Resident.Id(assigneeId) },
)

fun List<TaskEntity>.toTasks() = map { it.toTask() }

fun Task.toTaskEntity(): TaskEntity =
    TaskEntity(
        id = id?.value ?: 0,
        name = requireNotNull(name) { "Task name cannot be null" },
        roomId = requireNotNull(roomId?.value) { "Task roomId cannot be null"},
        duration = requireNotNull(duration) { "Task duration cannot be null" },
        frequency = requireNotNull(frequency) { "Task frequency cannot be null" },
        scheduledDate = requireNotNull(scheduledDate) { "Task scheduledDate cannot be null" },
        urgency = requireNotNull(urgency) { "Task urgency cannot be null" },
        assigneeId = requireNotNull(assigneeId?.value) { "Task assigneeId cannot be null" },
    )

fun EnrichedTaskEntity.toTask() =
    Task(
        id = id,
        name = taskName,
        roomId = roomId,
        duration = duration,
        frequency = frequency,
        scheduledDate = scheduledDate,
        urgency = urgency,
        assigneeId = assigneeId,
    )