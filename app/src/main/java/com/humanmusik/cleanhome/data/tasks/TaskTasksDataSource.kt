package com.humanmusik.cleanhome.data.tasks

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.humanmusik.cleanhome.workers.TaskSyncWorker

class TaskTasksDataSource(private val context: Context) {
    fun retrieveTasksOnStartup() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val taskSyncWorker =
            OneTimeWorkRequestBuilder<TaskSyncWorker>()
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                uniqueWorkName = TASK_SYNC_WORKER,
                existingWorkPolicy = ExistingWorkPolicy.KEEP,
                request = taskSyncWorker,
            )
    }

    companion object {
        private const val TASK_SYNC_WORKER = "TaskSyncWorker"
    }
}