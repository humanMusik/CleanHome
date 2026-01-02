package com.humanmusik.cleanhome.domain

import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.humanmusik.cleanhome.data.repository.cleanhome.CreateTask
import com.humanmusik.cleanhome.data.repository.cleanhome.CreateTask.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.CreateTaskInDb
import com.humanmusik.cleanhome.data.repository.cleanhome.CreateTaskInDb.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.DeleteTaskInDb
import com.humanmusik.cleanhome.data.repository.cleanhome.DeleteTaskInDb.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfAllResidents
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfAllResidents.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfTasks
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfTasks.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.UpdateTask
import com.humanmusik.cleanhome.data.repository.cleanhome.UpdateTask.Companion.invoke
import com.humanmusik.cleanhome.data.repository.cleanhome.UpdateTaskInDb
import com.humanmusik.cleanhome.data.repository.cleanhome.UpdateTaskInDb.Companion.invoke
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.State
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskCreationParcelData
import com.humanmusik.cleanhome.presentation.taskdetails.TaskEditParcelData
import com.humanmusik.cleanhome.workers.FinalizeTaskReassignmentWorker
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface TaskEditor {
    suspend fun reassignTask(task: Task): Task

    suspend fun undoTaskReassignment(
        originalTaskId: Task.Id,
        newTaskId: Task.Id
    )

    suspend fun assignTask(
        taskCreationParcelData: TaskCreationParcelData,
    )

    suspend fun editTask(
        taskEditParcelData: TaskEditParcelData,
    )

    suspend fun deactivateTask(taskId: Task.Id)
}

class TaskEditorImpl @Inject constructor(
    private val flowOfTasks: FlowOfTasks,
    private val flowOfAllResidents: FlowOfAllResidents,
    private val updateTask: UpdateTask,
    private val updateTaskInDb: UpdateTaskInDb,
    private val createTask: CreateTask,
    private val createTaskInDb: CreateTaskInDb,
    private val deleteTaskInDb: DeleteTaskInDb,
    private val workManager: WorkManager,
) : TaskEditor {
    override suspend fun reassignTask(task: Task): Task {
        val todaysDate = LocalDate.now()

        Log.d("Lezz", "reassignTask lastCompletedDate ${task.lastCompletedDate}")
        if (task.lastCompletedDate == todaysDate) throw TaskEditorExceptions.AlreadyCompletedToday()

        // 1. Move task to a Reversible state so it can be optimistically removed from UI
        updateTaskInDb(task.copy(state = State.Reversible))

        // 2. New properties for reassigned task
        val filter = TaskFilter.ByScheduledDate(
            startDateInclusive = todaysDate,
            endDateInclusive = getNewScheduledDate(
                dateCompleted = todaysDate,
                taskFrequency = task.frequency!!,
            )
        ) and TaskFilter.ByState(setOf(State.Active))

        val newTask = combine(
            flowOfTasks(filter),
            flowOfAllResidents(),
        ) { tasksBetweenDateCompletedAndNewDate, allResidents ->
            Task(
                id = Task.Id.generateNewId(),
                name = task.name,
                roomId = task.roomId,
                duration = task.duration,
                frequency = task.frequency,
                scheduledDate = getNewScheduledDate(
                    dateCompleted = todaysDate,
                    taskFrequency = task.frequency,
                ),
                urgency = task.urgency,
                assigneeId = getNewAssignment(
                    tasks = tasksBetweenDateCompletedAndNewDate.toList(),
                    allResidents = allResidents.map { requireNotNull(it.id) }
                ),
                state = State.Active,
                lastCompletedDate = todaysDate,
            )
        }
            .first()

        // 3. Create task in db
        createTaskInDb(newTask)
        Log.d("Lezz", "reassignTask createTaskInDb $newTask")

        // 4. Schedule a delayed worker to finalize the change in Firestore.
        // This is the key step to prevent an immediate sync.
        val workRequestTag =
            task.id?.let { FinalizeTaskReassignmentWorker.workRequestTag(originalTaskId = it) }
                ?: throw IllegalStateException("Task id cannot be null")

        val finalizeWorkRequest = OneTimeWorkRequestBuilder<FinalizeTaskReassignmentWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS) // A 10-second window to "Undo"
            .addTag(workRequestTag)
            .setInputData(
                workDataOf(
                    "ORIGINAL_TASK_ID" to task.id.value,
                    "NEW_TASK_ID" to newTask.id?.value,
                )
            )
            .build()

        workManager.enqueue(finalizeWorkRequest)

        return newTask
    }

    override suspend fun undoTaskReassignment(
        originalTaskId: Task.Id,
        newTaskId: Task.Id,
    ) {
        val workRequestTag = FinalizeTaskReassignmentWorker.workRequestTag(originalTaskId)

        workManager.cancelAllWorkByTag(workRequestTag)

        val originalTask =
            flowOfTasks(filter = TaskFilter.ById(setOf(originalTaskId))).first().first()

        updateTaskInDb(originalTask.copy(state = State.Active))
        deleteTaskInDb(newTaskId)
    }

    override suspend fun assignTask(
        taskCreationParcelData: TaskCreationParcelData,
    ) {
        val todaysDate = LocalDate.now()

        val filter = TaskFilter.ByScheduledDate(
            startDateInclusive = todaysDate,
            endDateInclusive = taskCreationParcelData.date!!,
        )

        val assignedTo = combine(
            flowOfTasks(filter),
            flowOfAllResidents(),
        ) { tasksBetweenTodaysDateAndScheduledDate, allResidents ->
            getNewAssignment(
                tasks = tasksBetweenTodaysDateAndScheduledDate.toList(),
                allResidents = allResidents.map { requireNotNull(it.id) }
            )
        }
            .first()

        val newTask = taskCreationParcelData.createTask(assigneeId = assignedTo)

        createTask(newTask)
    }

    override suspend fun editTask(taskEditParcelData: TaskEditParcelData) {
        val updatedTask = Task(
            id = taskEditParcelData.id,
            name = taskEditParcelData.taskName,
            roomId = taskEditParcelData.roomId,
            duration = taskEditParcelData.duration,
            frequency = taskEditParcelData.frequency,
            scheduledDate = taskEditParcelData.scheduledDate,
            urgency = taskEditParcelData.urgency,
            assigneeId = taskEditParcelData.assigneeId,
            state = State.Active,
            lastCompletedDate = taskEditParcelData.lastCompletedDate,
        )

        updateTask(updatedTask)
    }

    override suspend fun deactivateTask(taskId: Task.Id) {
        val taskToDelete = flowOfTasks(
            filter = TaskFilter.ById(setOf(taskId)),
        )
            .first()
            .first()
            .copy(
                state = State.Inactive
            )

        updateTask(task = taskToDelete)
    }

    private fun getNewAssignment(
        tasks: List<Task>,
        allResidents: List<Resident.Id>,
    ): Resident.Id {
        return tasks.mapOfResidentIdToTotalTaskDuration(allResidents)
            .minByOrNull { it.value }
            ?.key
            ?: allResidents.randomOrNull()
            ?: throw IllegalStateException()
    }

    private fun getNewScheduledDate(
        dateCompleted: LocalDate,
        taskFrequency: Frequency,
    ): LocalDate = when (taskFrequency) {
        Frequency.Daily -> dateCompleted.plusDays(1)
        Frequency.Weekly -> dateCompleted.plusDays(7)
        Frequency.Fortnightly -> dateCompleted.plusDays(14)
        Frequency.Monthly -> dateCompleted.plusMonths(1)
        Frequency.Quarterly -> dateCompleted.plusMonths(3)
        Frequency.BiAnnually -> dateCompleted.plusMonths(6)
        Frequency.Annually -> dateCompleted.plusYears(1)
    }

    private fun List<Task>.mapOfResidentIdToTotalTaskDuration(allResidents: List<Resident.Id>): Map<Resident.Id, Duration> {
        val tasks = this.toMutableList()
        allResidents.forEach { resident ->
            tasks.add(
                Task.build(
                    assigneeId = resident,
                    duration = Duration.ZERO,
                    state = State.Active
                )
            )
        }
        return tasks.groupingBy { it.assigneeId!! }
            .fold(Duration.ZERO) { acc, task ->
                acc + task.duration!!
            }
    }
}

sealed class TaskEditorExceptions : Throwable() {
    class AlreadyCompletedToday : TaskEditorExceptions()
}