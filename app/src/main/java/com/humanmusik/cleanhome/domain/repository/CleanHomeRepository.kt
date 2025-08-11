package com.humanmusik.cleanhome.domain.repository

import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.util.Resource
import kotlinx.coroutines.flow.Flow

fun interface CreateTask {
    suspend fun createTask(task: Task)

    companion object {
        suspend operator fun CreateTask.invoke(task: Task) =
            createTask(task)
    }
}

fun interface UpdateTask {
    suspend fun updateTask(task: Task)

    companion object {
        suspend operator fun UpdateTask.invoke(task: Task) =
            updateTask(task)
    }
}

fun interface FlowOfTasks {
    fun flowOfTasks(filter: TaskFilter): Flow<Set<Task>>

    companion object {
        operator fun FlowOfTasks.invoke(filter: TaskFilter) =
            flowOfTasks(filter)
    }
}

fun interface FlowOfAllResidents {
    fun flowOfAllResidents(): Flow<Set<Resident>>

    companion object {
        operator fun FlowOfAllResidents.invoke() =
            flowOfAllResidents()
    }
}
