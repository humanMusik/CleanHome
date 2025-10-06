package com.humanmusik.cleanhome.data.repository

import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.domain.EnrichedTaskFilter
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.ActionType
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.TaskLog
import com.humanmusik.cleanhome.domain.model.task.Task
import kotlinx.coroutines.flow.Flow

fun interface SyncTasks {
    suspend fun syncTasks()

    companion object {
        suspend operator fun SyncTasks.invoke() =
            syncTasks()
    }
}

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
    fun flowOfTaskLogsByTaskId(taskId: Task.Id): Flow<List<TaskLog>>

    companion object {
        operator fun FlowOfTaskLogsByTaskId.invoke(taskId: Task.Id) =
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

fun interface SyncRooms {
    suspend fun syncRooms()

    companion object {
        suspend operator fun SyncRooms.invoke() =
            syncRooms()
    }
}

fun interface FlowOfResidentById {
    fun flowOfResidentById(residentId: Resident.Id): Flow<Resident>

    companion object {
        operator fun FlowOfResidentById.invoke(residentId: Resident.Id) =
            flowOfResidentById(residentId)
    }
}

fun interface FlowOfRoomById {
    fun flowOfRoomById(roomId: Room.Id): Flow<Room>

    companion object {
        operator fun FlowOfRoomById.invoke(roomId: Room.Id) =
            flowOfRoomById(roomId)
    }
}

fun interface FlowOfEnrichedTasks {
    fun flowOfEnrichedTasks(filter: EnrichedTaskFilter): Flow<List<EnrichedTaskEntity>>

    companion object {
        operator fun FlowOfEnrichedTasks.invoke(filter: EnrichedTaskFilter) =
            flowOfEnrichedTasks(filter)
    }
}

fun interface FlowOfEnrichedTaskById {
    fun flowOfEnrichedTaskById(taskId: Task.Id): Flow<EnrichedTaskEntity>

    companion object {
        operator fun FlowOfEnrichedTaskById.invoke(taskId: Task.Id) =
            flowOfEnrichedTaskById(taskId)
    }
}
