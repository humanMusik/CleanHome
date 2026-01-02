package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.User
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.util.toEpochMillis

fun Task.toFirestoreTaskModel() = com.humanmusik.cleanhome.data.network.task.Task(
    id = id?.value ?: Task.Id.generateNewId().value,
    name = name,
    roomId = roomId?.value.toString(),
    duration = duration.toString(),
    scheduledDate = scheduledDate?.toEpochMillis().toString(),
    frequency = frequency?.name,
    urgent = urgency?.isUrgent(),
    assigneeId = assigneeId?.value.toString(),
    state = state?.name,
    lastCompletedDate = lastCompletedDate?.toEpochMillis().toString(),
)

fun Room.toFirestoreRoomModel() = com.humanmusik.cleanhome.data.network.room.Room(
    id = id?.value,
    name = name,
)

fun User.toFirestoreUserModel() = com.humanmusik.cleanhome.data.network.user.User(
    id = requireNotNull(id) { "userId cannot be null" },
    firstName = firstName,
    lastName = lastName,
    homes = emptyList() // revisit,
)

