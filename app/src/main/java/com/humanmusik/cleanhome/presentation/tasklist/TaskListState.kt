package com.humanmusik.cleanhome.presentation.tasklist

import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.presentation.FlowState

data class TaskListState(
    val enrichedTaskEntities: FlowState<List<EnrichedTaskEntity>>,
)
