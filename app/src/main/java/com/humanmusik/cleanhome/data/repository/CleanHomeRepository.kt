package com.humanmusik.cleanhome.data.repository

import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.ActionType
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.TaskLog
import com.humanmusik.cleanhome.domain.model.task.Task
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

fun interface FlowOfAllRooms {
    fun flowOfAllRooms(): Flow<List<Room>>

    companion object {
        operator fun FlowOfAllRooms.invoke() = flowOfAllRooms()
    }
}

fun interface CreateTaskLog {
    suspend fun createTaskLog(taskLog: TaskLog)

    companion object {
        suspend operator fun CreateTaskLog.invoke(taskLog: TaskLog) =
            createTaskLog(taskLog)
    }
}

fun interface FlowOfTaskLogsByTaskId {
    fun flowOfTaskLogsByTaskId(taskId: Int): Flow<List<TaskLog>>

    companion object {
        operator fun FlowOfTaskLogsByTaskId.invoke(taskId: Int) =
            flowOfTaskLogsByTaskId(taskId)
    }
}

fun interface FlowOfTaskLogsByActionType {
    fun flowOfTaskLogsByActionType(actionType: ActionType): Flow<List<TaskLog>>

    companion object {
        operator fun FlowOfTaskLogsByActionType.invoke(actionType: ActionType) =
            flowOfTaskLogsByActionType(actionType)
    }
}
