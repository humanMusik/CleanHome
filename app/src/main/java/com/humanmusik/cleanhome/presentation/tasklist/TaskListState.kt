package com.humanmusik.cleanhome.presentation.tasklist

import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity

data class TaskListState(
    val enrichedTaskEntities: List<EnrichedTaskEntity>,
)
