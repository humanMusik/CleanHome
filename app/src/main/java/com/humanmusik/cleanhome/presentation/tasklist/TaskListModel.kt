package com.humanmusik.cleanhome.presentation.tasklist

import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.FlowState

data class TaskListModel(
    val tasks: FlowState<List<Task>>,
)
