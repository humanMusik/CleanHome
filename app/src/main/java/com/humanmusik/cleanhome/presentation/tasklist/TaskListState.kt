package com.humanmusik.cleanhome.presentation.tasklist

import com.humanmusik.cleanhome.domain.model.task.Task

data class TaskListState(
    val tasks: List<Task>,
)
