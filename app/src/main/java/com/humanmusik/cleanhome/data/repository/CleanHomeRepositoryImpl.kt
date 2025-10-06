package com.humanmusik.cleanhome.data.repository

import com.humanmusik.cleanhome.data.CleanHomeDatabase
import com.humanmusik.cleanhome.data.api.task.TaskApi
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.data.mappers.toFirestoreTaskModel
import com.humanmusik.cleanhome.data.mappers.toResident
import com.humanmusik.cleanhome.data.mappers.toResidents
import com.humanmusik.cleanhome.data.mappers.toRoom
import com.humanmusik.cleanhome.data.mappers.toTaskEntities
import com.humanmusik.cleanhome.data.mappers.toTaskEntity
import com.humanmusik.cleanhome.data.mappers.toTaskLogEntity
import com.humanmusik.cleanhome.data.mappers.toTaskLogs
import com.humanmusik.cleanhome.data.mappers.toTasks
import com.humanmusik.cleanhome.di.ApplicationScope
import com.humanmusik.cleanhome.domain.EnrichedTaskFilter
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.TaskLog
import com.humanmusik.cleanhome.domain.model.task.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
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
    private val taskApi: TaskApi,
    db: CleanHomeDatabase,
) : SyncTasks,
    CreateTask,
    UpdateTask,
    FlowOfTasks,
    FlowOfAllResidents,
    FlowOfAllRooms,
    CreateTaskLog,
    FlowOfTaskLogsByTaskId,
    FlowOfRoomById,
    FlowOfResidentById,
    FlowOfEnrichedTasks,
    FlowOfEnrichedTaskById {

    private val dao = db.cleanHomeDao()

    override suspend fun syncTasks() {
        taskApi
            .listTasks()
            .map { dao.deleteAndInsertTasks(it.toTaskEntities()) }
            .launchIn(scope)
    }

    override suspend fun createTask(task: Task) {
        scope.launch {
            taskApi.uploadTask(task.toFirestoreTaskModel())
        }
    }

    override suspend fun updateTask(task: Task) {
        // We want this to launch in application scope so that it doesn't cancel when the VM is
        // cleared in the viewModelScope
        scope.launch {
            taskApi.editTask(task.toFirestoreTaskModel())
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

    private val allEnrichedTasks: Flow<List<EnrichedTaskEntity>> = flow {
        dao
            .flowOfEnrichedTasks()
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

    override fun flowOfTasks(filter: TaskFilter): Flow<Set<Task>> {
        return allTasks.map { tasks ->
            tasks
                .filter { task ->
                    filter.getFilterPredicate()(task)
                }
                .sorted()
                .toSet()
        }
            .distinctUntilChanged()
    }

    override fun flowOfEnrichedTasks(filter: EnrichedTaskFilter): Flow<List<EnrichedTaskEntity>> {
        return allEnrichedTasks
            .map { enrichedTasks ->
                enrichedTasks.filter { enrichedTask ->
                    filter.getFilterPredicate()(enrichedTask)
                }
                    .sorted()
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
            is TaskFilter.AllOf -> {
                { task ->
                    this.filters.all {
                        it.getFilterPredicate()(task)
                    }
                }
            }

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

            is TaskFilter.ByState -> {
                { this.states.contains(it.state) }
            }
        }
    }

    private fun EnrichedTaskFilter.getFilterPredicate(): (EnrichedTaskEntity) -> Boolean {
        return when (this) {
            is EnrichedTaskFilter.All -> {
                { true }
            }

            is EnrichedTaskFilter.ById -> {
                { this.ids.contains(it.id) }
            }

            is EnrichedTaskFilter.ByAssignment -> {
                { this.residents.contains(it.assigneeId) }
            }

            is EnrichedTaskFilter.ByScheduledDate -> {
                {
                    it.scheduledDate.isBetween(this.startDateInclusive, this.endDateInclusive)
                }
            }

            is EnrichedTaskFilter.ByState -> {
                { this.states.contains(it.state) }
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

    override fun flowOfTaskLogsByTaskId(taskId: Task.Id): Flow<List<TaskLog>> {
        return dao
            .getLogsByTaskId(requireNotNull(taskId.value))
            .map { it.toTaskLogs() }
    }

    override fun flowOfRoomById(roomId: Room.Id): Flow<Room> {
        return dao
            .flowOfRoomById(requireNotNull(roomId.value))
            .map { it.toRoom() }
    }

    override fun flowOfResidentById(residentId: Resident.Id): Flow<Resident> {
        return dao
            .flowOfResidentById(requireNotNull(residentId.value))
            .map { it.toResident() }
    }

    override fun flowOfEnrichedTaskById(taskId: Task.Id): Flow<EnrichedTaskEntity> {
        return dao.flowOfEnrichedTaskById(taskId.value)
    }
}
