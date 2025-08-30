package com.humanmusik.cleanhome.data.mappers

import com.humanmusik.cleanhome.data.entities.TaskLogEntity
import com.humanmusik.cleanhome.domain.model.TaskLog

fun List<TaskLogEntity>.toTaskLogs() =
    map { it.toTaskLog() }

fun TaskLogEntity.toTaskLog() =
    TaskLog(
        id = id,
        taskId = taskId,
        date = date,
        recordedAction = recordedAction,
    )

fun List<TaskLog>.toTaskLogEntities() =
    map { it.toTaskLogEntity() }

fun TaskLog.toTaskLogEntity() =
    TaskLogEntity(
        id = id ?: 0,
        taskId = taskId,
        date = date,
        recordedAction = recordedAction,
    )