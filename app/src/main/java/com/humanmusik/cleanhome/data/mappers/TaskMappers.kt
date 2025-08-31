package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.TaskEntity
import com.humanmusik.cleanhome.domain.model.ResidentId
import com.humanmusik.cleanhome.domain.model.RoomId
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskId

fun TaskEntity.toTask() = Task(
    id = TaskId(id),
    name = name,
    roomId = RoomId(roomId),
    duration = duration,
    frequency = frequency,
    scheduledDate = scheduledDate,
    urgency = urgency,
    assigneeId = ResidentId(assigneeId),
)

fun List<TaskEntity>.toTasks() = map { it.toTask() }

fun Task.toTaskEntity(): TaskEntity =
    TaskEntity(
        id = id.value ?: 0,
        name = requireNotNull(name) { "Task name cannot be null" },
        roomId = requireNotNull(roomId.value),
        duration = requireNotNull(duration) { "Task duration cannot be null" },
        frequency = requireNotNull(frequency) { "Task frequency cannot be null" },
        scheduledDate = requireNotNull(scheduledDate) { "Task scheduledDate cannot be null" },
        urgency = requireNotNull(urgency) { "Task urgency cannot be null" },
        assigneeId = assigneeId.value,
    )
