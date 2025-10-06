package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.util.toEpochMillis

fun Task.toFirestoreTaskModel() = com.humanmusik.cleanhome.data.api.task.Task(
    id = id?.value,
    name = name,
    roomId = roomId?.value.toString(),
    duration = duration.toString(),
    scheduledDate = scheduledDate?.toEpochMillis().toString(),
    frequency = frequency?.name,
    urgent = urgency?.isUrgent(),
    assigneeId = assigneeId?.value.toString(),
    state = state?.name,
)

fun Room.toFirestoreRoomModel() = com.humanmusik.cleanhome.data.api.room.Room(
    id = id?.value,
    name = name,
)
