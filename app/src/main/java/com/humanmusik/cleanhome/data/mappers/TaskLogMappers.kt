package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.TaskLogEntity
import com.humanmusik.cleanhome.domain.model.TaskLog
import com.humanmusik.cleanhome.domain.model.task.Task

fun List<TaskLogEntity>.toTaskLogs() =
    map { it.toTaskLog() }

fun TaskLogEntity.toTaskLog() =
    TaskLog(
        id = TaskLog.Id(id),
        taskId = Task.Id(taskId),
        date = date,
        recordedAction = recordedAction,
    )

fun List<TaskLog>.toTaskLogEntities() =
    map { it.toTaskLogEntity() }

fun TaskLog.toTaskLogEntity() =
    TaskLogEntity(
        id = id?.value ?: 0,
        taskId = taskId.value,
        date = date,
        recordedAction = recordedAction,
    )