package com.humanmusik.cleanhome.data.repository

import com.humanmusik.cleanhome.data.CleanHomeDatabase
import com.humanmusik.cleanhome.data.mappers.toResident
import com.humanmusik.cleanhome.data.mappers.toResidents
import com.humanmusik.cleanhome.data.mappers.toRoom
import com.humanmusik.cleanhome.data.mappers.toTaskEntity
import com.humanmusik.cleanhome.data.mappers.toTaskLogEntity
import com.humanmusik.cleanhome.data.mappers.toTaskLogs
import com.humanmusik.cleanhome.data.mappers.toTasks
import com.humanmusik.cleanhome.di.ApplicationScope
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.ResidentId
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.RoomId
import com.humanmusik.cleanhome.domain.model.TaskLog
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CleanHomeRepositoryImpl @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    db: CleanHomeDatabase,
) : CreateTask,
    UpdateTask,
    FlowOfTasks,
    FlowOfAllResidents,
    FlowOfAllRooms,
    CreateTaskLog,
    FlowOfTaskLogsByTaskId,
    FlowOfRoomById,
    FlowOfResidentById {

    private val dao = db.cleanHomeDao()

    override suspend fun createTask(task: Task) {
        scope.launch {
            dao.insertTask(task.toTaskEntity())
        }
    }

    override suspend fun updateTask(task: Task) {
        // We want this to launch in application scope so that it doesn't cancel when the VM is
        // cleared in the viewModelScope
        scope.launch {
            dao.updateTask(task.toTaskEntity())
        }
    }

    private val allTasks: Flow<List<Task>> = flow {
        dao.getAllTasks().map { it.toTasks() }
            .distinctUntilChanged()
            .shareIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(
                    stopTimeoutMillis = 0,
                    replayExpirationMillis = 0,
                ),
                replay = 1,
            )
            .collect(this@flow)
    }

    override fun flowOfAllResidents(): Flow<Set<Resident>> {
        return dao
            .flowOfAllResidents()
            .map { residents ->
                residents
                    .toResidents()
                    .toSet()
            }
    }

    override fun flowOfTasks(
        filter: TaskFilter,
    ): Flow<Set<Task>> {
        return allTasks.map { tasks ->
            tasks.filter { task ->
                filter.getFilterPredicate()(task)
            }
                .sorted()
                .toSet()
        }
            .distinctUntilChanged()
    }

    override fun flowOfAllRooms(): Flow<List<Room>> {
        val rooms = dao.flowOfAllRooms()
            .onEach { println("repo rooms: $it") }
        return rooms
            .distinctUntilChanged()
            .mapContent {
                it.toRoom()
            }
    }

    private fun TaskFilter.getFilterPredicate(): (Task) -> Boolean {
        return when (this) {
            is TaskFilter.All -> {
                { true }
            }

            is TaskFilter.ById -> {
                { this.ids.contains(it.id) }
            }

            is TaskFilter.ByAssignment -> {
                { this.residents.contains(it.assigneeId) }
            }

            is TaskFilter.ByScheduledDate -> {
                {
                    it.scheduledDate?.isBetween(this.startDateInclusive, this.endDateInclusive)
                        ?: false
                }
            }
        }
    }

    private fun LocalDate.isBetween(
        startDateInclusive: LocalDate,
        endDateInclusive: LocalDate,
    ): Boolean {
        return isEqual(startDateInclusive) ||
                (isAfter(startDateInclusive) && isBefore(endDateInclusive)) ||
                isEqual(endDateInclusive)
    }

    private inline fun <T, R> Flow<Iterable<T>>.mapContent(
        crossinline transform: (T) -> R,
    ): Flow<List<R>> {
        return map { it.map(transform) }
    }

    override suspend fun createTaskLog(taskLog: TaskLog) {
        dao.insertTaskLog(taskLog.toTaskLogEntity())
    }

    override fun flowOfTaskLogsByTaskId(taskId: TaskId): Flow<List<TaskLog>> {
        return dao
            .getLogsByTaskId(requireNotNull(taskId.value))
            .map { it.toTaskLogs() }
    }

    override fun flowOfRoomById(roomId: RoomId): Flow<Room> {
        return dao
            .flowOfRoomById(requireNotNull(roomId.value))
            .map { it.toRoom() }
    }

    override fun flowOfResidentById(residentId: ResidentId): Flow<Resident> {
        return dao
        .flowOfResidentById(requireNotNull(residentId.value))
            .map { it.toResident() }
    }
}
