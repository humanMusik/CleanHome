package com.humanmusik.cleanhome.data.repository

import com.humanmusik.cleanhome.data.CleanHomeDatabase
import com.humanmusik.cleanhome.data.mappers.toTask
import com.humanmusik.cleanhome.data.mappers.toTaskEntity
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.repository.CleanHomeRepository
import com.humanmusik.cleanhome.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CleanHomeRepositoryImpl @Inject constructor(
    db: CleanHomeDatabase,
): CleanHomeRepository {
    private val dao = db.cleanHomeDao()

    override suspend fun getAllTasks(): Flow<Resource<List<Task>>> {

        return flow {
            emit(Resource.Loading(true))
            val localTasks = dao.getAllTasks()
            emit(
                Resource.Success(
                    data = localTasks.map { it.toTask() },
                )
            )

//            val isDbEmpty = localTasks.isEmpty()
//            val shouldLoadFromCache = !isDbEmpty && !fetchFromRemote
//            if (shouldLoadFromCache) {
//                emit(Resource.Loading(false))
//                return@flow
//            }
        }
    }

    override suspend fun updateTask(task: Task){
        dao.upsertTask(task.toTaskEntity())
    }
}