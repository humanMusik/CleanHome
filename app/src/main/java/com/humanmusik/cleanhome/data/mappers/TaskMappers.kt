package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.TaskEntity
import com.humanmusik.cleanhome.domain.model.Task

fun TaskEntity.toTask() = Task(
    id = id,
    name = name,
    room = room.toRoom(),
    duration = duration,
    frequency = frequency,
    scheduledDate = scheduledDate,
    urgency = urgency,
    assignedTo = resident.toResident()
)