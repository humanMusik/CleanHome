package com.humanmusik.cleanhome.domain.model.task

import com.humanmusik.cleanhome.domain.TaskFilter
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents.Companion.invoke
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks.Companion.invoke
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

interface TaskEditor {
    fun reassignTask(
        task: Task,
        dateCompleted: LocalDate,
    ): Flow<Task>
}

class TaskEditorImpl @Inject constructor(
    private val flowOfTasks: FlowOfTasks,
    private val flowOfAllResidents: FlowOfAllResidents,
) : TaskEditor {
    override fun reassignTask(
        task: Task,
        dateCompleted: LocalDate,
    ): Flow<Task> {
        val filter = TaskFilter.ByScheduledDate(
            startDateInclusive = dateCompleted,
            endDateInclusive = getNewScheduledDate(
                dateCompleted = dateCompleted,
                taskFrequency = task.frequency,
            ),
        )

        return combine(
            flowOfTasks(filter),
            flowOfAllResidents(),
        ) { tasksBetweenDateCompletedAndNewDate, allResidents ->
            Task(
                id = task.id,
                name = task.name,
                room = task.room,
                duration = task.duration,
                frequency = task.frequency,
                scheduledDate = getNewScheduledDate(
                    dateCompleted = dateCompleted,
                    taskFrequency = task.frequency,
                ),
                urgency = task.urgency,
                assignedTo = getNewAssignment(
                    tasks = tasksBetweenDateCompletedAndNewDate,
                    allResidents = allResidents
                ),
            )
        }
    }

    private fun getNewAssignment(
        tasks: Set<Task>,
        allResidents: Set<Resident>,
    ): Resident {
        return tasks.mapOfResidentToTotalTaskDuration()
            .minByOrNull { it.value }
            ?.key
            ?: allResidents.random()
        // TODO: handle scenario when allResidents is empty
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
        Frequency.Annually -> dateCompleted.plusMonths(12)
    }

    private fun Set<Task>.mapOfResidentToTotalTaskDuration(): Map<Resident, Duration> =
        groupingBy { it.assignedTo }
            .fold(0.minutes) { acc, task ->
                acc + task.duration
            }
}