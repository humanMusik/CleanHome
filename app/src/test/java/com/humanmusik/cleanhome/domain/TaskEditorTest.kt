package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.domain.model.Resident as ResidentDomain
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskEditorImpl
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.domain.repository.FlowOfTasksForResident
import com.humanmusik.cleanhome.utilstest.assertIsEqualTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class TaskEditorTest {
    @Test
    fun `reassignTask() - reassigned task has the same id`() {
        val originalTask = task(id = 1)

        taskEditor().reassignTask(originalTask).id assertIsEqualTo 1
    }

    @Test
    fun `reassignTask() - reassigned task has the same name`() {
        val originalTask = task(name = "Vacuum")

        taskEditor().reassignTask(originalTask).name assertIsEqualTo "Vacuum"
    }

    @Test
    fun `reassignTask() - reassigned task has the same room`() {
        val originalTask = task(
            room = Room(
                id = 1,
                name = "Living Room",
                homeId = 1,
            )
        )

        taskEditor().reassignTask(originalTask).room assertIsEqualTo
                Room(
                    id = 1,
                    name = "Living Room",
                    homeId = 1,
                )
    }

    @Test
    fun `reassignTask() - reassigned task has the same duration`() {
        val originalTask = task(duration = 1000L)

        taskEditor().reassignTask(originalTask).duration assertIsEqualTo 1000L
    }

    @Test
    fun `reassignTask() - reassigned task has the same urgency`() {
        val originalTask = task(urgency = Urgency.Urgent)

        taskEditor().reassignTask(originalTask).urgency assertIsEqualTo Urgency.Urgent
    }

    @Test
    fun `reassignTask() - scheduled date is 1 day from original date if frequency is daily`() {
        val originalTask = task(
            scheduledDate = LocalDate.of(2026, 7, 7),
            frequency = Frequency.Daily,
        )

        taskEditor().reassignTask(originalTask).scheduledDate assertIsEqualTo
                LocalDate.of(2026, 7, 8)
    }

    @Test
    fun `reassignTask() - scheduled date is 7 days from original date if frequency is weekly`() {
        val originalTask = task(
            scheduledDate = LocalDate.of(2026, 7, 7),
            frequency = Frequency.Weekly,
        )

        taskEditor().reassignTask(originalTask).scheduledDate assertIsEqualTo
                LocalDate.of(2026, 7, 14)
    }

    @Test
    fun `reassignTask() - scheduled date is 14 days from original date if frequency is fortnightly`() {
        val originalTask = task(
            scheduledDate = LocalDate.of(2026, 7, 7),
            frequency = Frequency.Fortnightly,
        )

        taskEditor().reassignTask(originalTask).scheduledDate assertIsEqualTo
                LocalDate.of(2026, 7, 21)
    }

    @Test
    fun `reassignTask() - scheduled date is 1 month from original date if frequency is monthly`() {
        val originalTask = task(
            scheduledDate = LocalDate.of(2026, 7, 7),
            frequency = Frequency.Monthly,
        )

        taskEditor().reassignTask(originalTask).scheduledDate assertIsEqualTo
                LocalDate.of(2026, 8, 7)
    }

    @Test
    fun `reassignTask() - scheduled date is 3 months from original date if frequency is quarterly`() {
        val originalTask = task(
            scheduledDate = LocalDate.of(2026, 7, 7),
            frequency = Frequency.Quarterly,
        )

        taskEditor().reassignTask(originalTask).scheduledDate assertIsEqualTo
                LocalDate.of(2026, 10, 7)
    }

    @Test
    fun `reassignTask() - scheduled date is 6 months from original date if frequency is bi-anually`() {
        val originalTask = task(
            scheduledDate = LocalDate.of(2026, 7, 7),
            frequency = Frequency.Quarterly,
        )

        taskEditor().reassignTask(originalTask).scheduledDate assertIsEqualTo
                LocalDate.of(2027, 1, 7)
    }

    @Test
    fun `reassignTask() - scheduled date is 1 year from original date if frequency is anually`() {
        val originalTask = task(
            scheduledDate = LocalDate.of(2026, 7, 7),
            frequency = Frequency.Quarterly,
        )

        taskEditor().reassignTask(originalTask).scheduledDate assertIsEqualTo
                LocalDate.of(2027, 7, 7)
    }

    @Test
    fun `reassignTask() - assign task to resident with least workload in next 1 day if frequency is daily`() {
        val originalTask = task(
            scheduledDate = LocalDate.of(2026, 6, 6),
            frequency = Frequency.Daily,
        )
        val allTasksForResident1 = listOf(
            task(
                scheduledDate = LocalDate.of(2026, 6, 7),
                frequency = Frequency.Weekly,
                duration = 20.minutes,
            ),
            task(
                scheduledDate = LocalDate.of(2026, 6, 7),
                frequency = Frequency.BiAnnually,
                duration = 2.hours,
            )
        )
        val allTasksForResident2 = listOf(
            task(
                scheduledDate = LocalDate.of(2026, 6, 7),
                frequency = Frequency.Weekly,
                duration = 30.minutes,
            ),
        )
        val allTasksForResident3 = listOf(
            task(
                scheduledDate = LocalDate.of(2026, 6, 7),
                frequency = Frequency.Annually,
                duration = 4.hours,
            ),
            task(
                scheduledDate = LocalDate.of(2026, 6, 7),
                frequency = Frequency.Monthly,
                duration = 2.hours,
            )
        )

        val residentWithLeastWorkload = Resident.Three.value

        val mapOfResidentIdToTasks = taskMapper(
            1 to flowOf(allTasksForResident1),
            2 to flowOf(allTasksForResident2),
            3 to flowOf(allTasksForResident3),
        )

        taskEditor(
            allResidents = allResidents,
            taskMapper = mapOfResidentIdToTasks,
        )
            .reassignTask(task = originalTask)
            .assignedTo assertIsEqualTo residentWithLeastWorkload
    }

    private val allResidents: List<ResidentDomain> = listOf(
        Resident.One.value,
        Resident.Two.value,
        Resident.Three.value,
    )

    object Resident {
        object One {
            val value = ResidentDomain(
                id = 1,
                name = "John Smith",
                homeId = 1,
            )
        }
        object Two {
            val value = ResidentDomain(
                id = 2,
                name = "Erwin Smith",
                homeId = 1,
            )
        }
        object Three {
            val value = ResidentDomain(
                id = 3,
                name = "David Smith",
                homeId = 1,
            )
        }
    }

    private fun task(
        id: Int = 1,
        name: String = "Vacuum",
        room: Room = Room(
            id = 1,
            name = "Living Room",
            homeId = 1,
        ),
        duration: Duration = 30.minutes,
        frequency: Frequency = Frequency.Weekly,
        scheduledDate: LocalDate = LocalDate.of(2026, 7, 7),
        urgency: Urgency = Urgency.Urgent,
        assignedTo: ResidentDomain = ResidentDomain(
            id = 1,
            name = "John Smith",
            homeId = 1,
        ),
    ) = Task(
        id = id,
        name = name,
        room = room,
        duration = duration,
        frequency = frequency,
        scheduledDate = scheduledDate,
        urgency = urgency,
        assignedTo = assignedTo,
    )

    private fun taskEditor(
        allResidents: List<ResidentDomain>,
        taskMapper: Int.() -> Flow<List<Task>>,
    ): TaskEditorImpl {
        val flowOfTasksForResident = FlowOfTasksForResident { residentId ->
            residentId.taskMapper()
        }
        return TaskEditorImpl(
            flowOfAllResidents = { flowOf(allResidents) },
            flowOfTasksForResident = flowOfTasksForResident,
        )
    }

    private fun taskMapper(vararg inputs: Pair<Int, Flow<List<Task>>>): Int.() -> Flow<List<Task>> {
        val outputByReceiver: Map<Int, Flow<List<Task>>> = inputs.toMap()
        return {
            outputByReceiver[this] ?: throw IllegalStateException("Unexpected result $this")
        }
    }
}