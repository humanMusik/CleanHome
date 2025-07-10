package com.humanmusik.cleanhome.domain.repository

import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.util.Resource
import kotlinx.coroutines.flow.Flow

interface CleanHomeRepository {
    suspend fun updateTask(task: Task)
}

fun interface FlowOfAllTasks {
    fun flowOfAllTasks(): Flow<Resource<List<Task>>>

    companion object {
        operator fun FlowOfAllTasks.invoke() =
            flowOfAllTasks()
    }
}

fun interface FlowOfTasks {
    fun flowOfTasks(filter: TaskFilter): Flow<List<Task>>

    companion object {
        operator fun FlowOfTasks.invoke(filter: TaskFilter) =
            flowOfTasks(filter)
    }
}

fun interface FlowOfAllResidents {
    fun flowOfAllResidents(): Flow<List<Resident>>

    companion object {
        operator fun FlowOfAllResidents.invoke() =
            flowOfAllResidents()
    }
}

fun interface FlowOfTasksForResident {
    fun flowOfTasksForResident(residentId: Int): Flow<List<Task>>

    companion object {
        operator fun FlowOfTasksForResident.invoke(residentId: Int) =
            flowOfTasksForResident(residentId)
    }
}