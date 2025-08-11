package com.humanmusik.cleanhome.data.repository

import com.humanmusik.cleanhome.data.CleanHomeDatabase
import com.humanmusik.cleanhome.data.mappers.toResident
import com.humanmusik.cleanhome.data.mappers.toTaskEntity
import com.humanmusik.cleanhome.data.mappers.toTasks
import com.humanmusik.cleanhome.di.ApplicationScope
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.repository.CreateTask
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks
import com.humanmusik.cleanhome.domain.repository.UpdateTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
    FlowOfAllResidents {

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
        return flow {
            emit(
                dao
                    .getAllResidentsWithMetadata()
                    .map { entity ->
                        entity.toResident()
                    }
                    .toSet()
            )
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

    private fun TaskFilter.getFilterPredicate(): (Task) -> Boolean {
        return when (this) {
            is TaskFilter.All -> {
                { true }
            }

            is TaskFilter.ById -> {
                { this.ids.contains(it.id) }
            }

            is TaskFilter.ByAssignment -> {
                { this.residents.contains(it.assignedTo) }
            }

            is TaskFilter.ByScheduledDate -> {
                { it.scheduledDate.isBetween(this.startDateInclusive, this.endDateInclusive) }
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
}