package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.TaskEntity
import com.humanmusik.cleanhome.domain.model.task.Task

fun TaskEntity.toTask() = Task(
    id = id,
    name = name,
    room = room.toRoom(),
    duration = duration,
    frequency = frequency,
    scheduledDate = scheduledDate,
    urgency = urgency,
    assignedTo = assignedTo.toResident()
)

fun List<TaskEntity>.toTasks() = map { it.toTask() }

fun Task.toTaskEntity() = TaskEntity(
    id = id ?: 0,
    name = name,
    room = room.toRoomEntity(),
    duration = duration,
    frequency = frequency,
    scheduledDate = scheduledDate,
    urgency = urgency,
    assignedTo = assignedTo.toResidentEntity(),
)