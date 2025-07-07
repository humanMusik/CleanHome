package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskEditorImpl
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.utilstest.assertIsEqualTo
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

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

    private val allResidents = listOf(
        Resident(
            id = 1,
            name = "John Smith",
            homeId = 1,
        ),
        Resident(
            id = 2,
            name = "Erwin Smith",
            homeId = 1,
        ),
        Resident(
            id = 3,
            name = "David Smith",
            homeId = 1,
        ),
    )

    private fun task(
        id: Int = 1,
        name: String = "Vacuum",
        room: Room = Room(
            id = 1,
            name = "Living Room",
            homeId = 1,
        ),
        duration: Long = 1000L,
        frequency: Frequency = Frequency.Weekly,
        scheduledDate: LocalDate = LocalDate.of(2026, 7, 7),
        urgency: Urgency = Urgency.Urgent,
        assignedTo: Resident = Resident(
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

    private fun taskEditor() =
        TaskEditorImpl(allResidents = allResidents)
}