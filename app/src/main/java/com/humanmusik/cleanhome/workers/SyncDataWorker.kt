package com.humanmusik.cleanhome.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncHomes
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncHomes.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncResidents
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncResidents.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncRooms
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncRooms.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncTasks
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncTasks.Companion.invoke
import com.humanmusik.cleanhome.domain.HasReversibleTasks
import com.humanmusik.cleanhome.domain.HasReversibleTasks.Companion.invoke
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncHomes: SyncHomes,
    private val syncTasks: SyncTasks,
    private val syncRooms: SyncRooms,
    private val syncResidents: SyncResidents,
    private val hasReversibleTasks: HasReversibleTasks,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            syncHomes()
            syncRooms()
            syncResidents()

            // syncTasks must occur after syncHomes and syncRooms due to foreign key constraints
            if (!hasReversibleTasks()) {
                syncTasks()
            }
            Result.success()
        } catch (e: Exception) {
            // If the sync fails, you can retry based on your policy
            Result.retry()
        }
    }
}
