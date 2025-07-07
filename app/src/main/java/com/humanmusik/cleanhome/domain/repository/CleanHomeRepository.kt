package com.humanmusik.cleanhome.domain.repository

import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.util.Resource
import kotlinx.coroutines.flow.Flow

interface CleanHomeRepository {
    suspend fun getAllTasks(): Flow<Resource<List<Task>>>

    suspend fun updateTask(task: Task)
}