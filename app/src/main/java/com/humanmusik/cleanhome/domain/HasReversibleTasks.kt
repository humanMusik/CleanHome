package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfTasks
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfTasks.Companion.invoke
import com.humanmusik.cleanhome.domain.model.task.State
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

fun interface HasReversibleTasks {
    suspend fun hasReversibleTasks(): Boolean

    companion object {
        suspend operator fun HasReversibleTasks.invoke() =
            hasReversibleTasks()
    }
}

class HasReversibleTasksImpl @Inject constructor(
    private val flowOfTasks: FlowOfTasks,
) : HasReversibleTasks {
    override suspend fun hasReversibleTasks(): Boolean {
        return flowOfTasks(filter = TaskFilter.ByState(setOf(State.Reversible)))
            .first()
            .isNotEmpty()
    }
}