package com.humanmusik.cleanhome.data.repository

import com.humanmusik.cleanhome.data.CleanHomeDatabase
import com.humanmusik.cleanhome.data.mappers.toResident
import com.humanmusik.cleanhome.data.mappers.toTask
import com.humanmusik.cleanhome.data.mappers.toTaskEntity
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.repository.CleanHomeRepository
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.domain.repository.FlowOfAllTasks
import com.humanmusik.cleanhome.domain.repository.FlowOfTasksForResident
import com.humanmusik.cleanhome.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CleanHomeRepositoryImpl @Inject constructor(
    db: CleanHomeDatabase,
) : CleanHomeRepository,
    FlowOfAllTasks,
    FlowOfAllResidents,
    FlowOfTasksForResident {
    private val dao = db.cleanHomeDao()

    override suspend fun updateTask(task: Task) {
        dao.upsertTask(task.toTaskEntity())
    }

    override fun flowOfAllTasks(): Flow<Resource<List<Task>>> {
        return flow {
            emit(Resource.Loading(true))
            val localTasks = dao.getAllTasks()
            emit(
                Resource.Success(
                    data = localTasks.map { it.toTask() },
                )
            )
        }
    }

    override fun flowOfAllResidents(): Flow<List<Resident>> {
        return flow {
            emit(
                dao
                    .getAllResidentsWithMetadata()
                    .map { entity ->
                        entity.toResident()
                    }
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
}