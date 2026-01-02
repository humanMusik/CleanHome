package com.humanmusik.cleanhome.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.humanmusik.cleanhome.data.repository.cleanhome.CreateTask
import com.humanmusik.cleanhome.data.repository.cleanhome.CreateTask.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.DeleteTask
import com.humanmusik.cleanhome.data.repository.cleanhome.DeleteTask.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfTasks
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfTasks.Companion.invoke
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.task.Task
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class FinalizeTaskReassignmentWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val createTask: CreateTask,
    private val deleteTask: DeleteTask,
    private val flowOfTasks: FlowOfTasks,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val originalTaskId = inputData
            .getString("ORIGINAL_TASK_ID")
            ?.let { Task.Id(it) }
            ?: return Result.failure()

        val newTaskId = inputData.getString("NEW_TASK_ID")
            ?.let { Task.Id(it) }
            ?: return Result.failure()

        try {
            val newTask = flowOfTasks(filter = TaskFilter.ById(setOf(newTaskId))).first().first()
            createTask(newTask)
            Log.d("Lezz", "FinalizeTaskReassignmentWorker createTask $newTask")
            deleteTask(originalTaskId)
            Log.d("Lezz", "FinalizeTaskReassignmentWorker deleteTask $originalTaskId")
            // sync tasks?
            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }

    companion object {
        fun workRequestTag(originalTaskId: Task.Id) = "FinalizeTaskReassignmentWorker_${originalTaskId.value}"
    }
}
