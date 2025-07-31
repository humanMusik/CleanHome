package com.humanmusik.cleanhome.data.repository

import com.humanmusik.cleanhome.data.CleanHomeDatabase
import com.humanmusik.cleanhome.data.mappers.toResident
import com.humanmusik.cleanhome.data.mappers.toTask
import com.humanmusik.cleanhome.data.mappers.toTaskEntity
import com.humanmusik.cleanhome.di.ApplicationScope
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks
import com.humanmusik.cleanhome.domain.repository.FlowOfTasksForResident
import com.humanmusik.cleanhome.domain.repository.UpdateTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CleanHomeRepositoryImpl @Inject constructor(
    @ApplicationScope scope: CoroutineScope,
    db: CleanHomeDatabase,
) : UpdateTask,
    FlowOfTasks,
    FlowOfAllResidents,
    FlowOfTasksForResident {
    private val dao = db.cleanHomeDao()

    override fun updateTask(task: Task) {
        dao.updateTask(task.toTaskEntity())
    }

    private val allTasks = flow {
        flow {
            emit(dao.getAllTasks().map { it.toTask() })
        }
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

    override fun flowOfTasksForResident(residentId: Int): Flow<List<Task>> {
        return flow {
            emit(
                dao
                    .getTasksForResident(residentId = residentId)
                    .map { entity ->
                        entity.toTask()
                    }
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
            TaskFilter.All -> { { true } }
            is TaskFilter.ByAssignment -> {
                { it.assignedTo == this.assignedTo }
            }
            is TaskFilter.ByScheduledDate -> {
                { it.scheduledDate.isBetween(this.startDateInclusive, this.endDateInclusive) }
            }
        }
    }

    private fun LocalDate.isBetween(startDateInclusive: LocalDate, endDateInclusive: LocalDate): Boolean {
        return isEqual(startDateInclusive) ||
                (isAfter(startDateInclusive) && isBefore(endDateInclusive)) ||
                isEqual(endDateInclusive)
    }
}