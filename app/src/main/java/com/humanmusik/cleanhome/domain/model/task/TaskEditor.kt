package com.humanmusik.cleanhome.domain.model.task

import com.humanmusik.cleanhome.data.repository.CreateTask
import com.humanmusik.cleanhome.data.repository.CreateTask.Companion.invoke
import com.humanmusik.cleanhome.data.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.data.repository.FlowOfAllResidents.Companion.invoke
import com.humanmusik.cleanhome.data.repository.FlowOfTaskLogsByTaskId
import com.humanmusik.cleanhome.data.repository.FlowOfTaskLogsByTaskId.Companion.invoke
import com.humanmusik.cleanhome.data.repository.FlowOfTasks
import com.humanmusik.cleanhome.data.repository.FlowOfTasks.Companion.invoke
import com.humanmusik.cleanhome.data.repository.UpdateTask
import com.humanmusik.cleanhome.data.repository.UpdateTask.Companion.invoke
import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskCreationParcelData
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

interface TaskEditor {
    suspend fun reassignTask(
        task: Task,
        dateCompleted: LocalDate,
    )

    suspend fun assignTask(
        taskCreationParcelData: TaskCreationParcelData,
        todayDate: LocalDate,
    )
}

class TaskEditorImpl @Inject constructor(
    private val flowOfTasks: FlowOfTasks,
    private val flowOfAllResidents: FlowOfAllResidents,
    private val updateTask: UpdateTask,
    private val createTask: CreateTask,
    private val flowOfLogsByTaskId: FlowOfTaskLogsByTaskId,
) : TaskEditor {
    override suspend fun reassignTask(
        task: Task,
        dateCompleted: LocalDate,
    ) {
        val filter = TaskFilter.ByScheduledDate(
            startDateInclusive = dateCompleted,
            endDateInclusive = getNewScheduledDate(
                dateCompleted = dateCompleted,
                taskFrequency = task.frequency!!,
            ),
        )

        val reAssignedTask = combine(
            flowOfTasks(filter),
            flowOfAllResidents(),
            flowOfLogsByTaskId(requireNotNull(task.id)),
        ) { tasksBetweenDateCompletedAndNewDate, allResidents, logs ->
            if (logs.any { it.wasCompletedOn(dateCompleted) }) throw TaskEditorExceptions.AlreadyCompletedToday()

            task.copy(
                scheduledDate = getNewScheduledDate(
                    dateCompleted = dateCompleted,
                    taskFrequency = task.frequency,
                ),
                assigneeId = getNewAssignment(
                    tasks = tasksBetweenDateCompletedAndNewDate.toList(),
                    allResidents = allResidents.map { requireNotNull(it.id) }
                ),
            )
        }
            .first()

        updateTask(reAssignedTask)
    }

    override suspend fun assignTask(
        taskCreationParcelData: TaskCreationParcelData,
        todayDate: LocalDate,
    ) {
        val filter = TaskFilter.ByScheduledDate(
            startDateInclusive = todayDate,
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
            tasks.add(Task.build(assigneeId = resident, duration = Duration.ZERO))
        }
        return tasks.groupingBy { it.assigneeId!! }
            .fold(0.minutes) { acc, task ->
                acc + task.duration!!
            }
    }
}

sealed class TaskEditorExceptions : Throwable() {
    class AlreadyCompletedToday : TaskEditorExceptions()
}