package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.data.repository.CreateTask
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskEditorImpl
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.data.repository.UpdateTask
import com.humanmusik.cleanhome.utilstest.assertIsEqualTo
import com.humanmusik.cleanhome.utilstest.runTest
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import com.humanmusik.cleanhome.domain.model.Resident as ResidentDomain

class TaskEditorImplTest {
    @Test
    fun `reassignTask() - reassigned task has the same id`() {
        runTest {
            val originalTask = task(id = 1)

            val expectedId = 1

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.id assertIsEqualTo expectedId
                }
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
        }
    }

    @Test
    fun `reassignTask() - reassigned task has the same name`() {
        runTest {
            val originalTask = task(name = "Vacuum")

            val expectedName = "Vacuum"

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.name assertIsEqualTo expectedName
                }
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)

        }
    }

    @Test
    fun `reassignTask() - reassigned task has the same room`() {
        runTest {
            val originalTask = task(
                room = Room(
                    id = 1,
                    name = "Living Room",
                    homeId = 1,
                )
            )

            val expectedRoom = Room(
                id = 1,
                name = "Living Room",
                homeId = 1,
            )

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.room assertIsEqualTo expectedRoom
                },
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)

        }
    }

    @Test
    fun `reassignTask() - reassigned task has the same duration`() {
        runTest {
            val originalTask = task(duration = 1.hours)

            val expectedDuration = 1.hours

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.duration assertIsEqualTo expectedDuration
                },
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
        }
    }

    @Test
    fun `reassignTask() - reassigned task has the same urgency`() {
        runTest {
            val originalTask = task(urgency = Urgency.Urgent)

            val expectedUrgency = Urgency.Urgent

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.urgency assertIsEqualTo expectedUrgency
                },
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 1 day from completed date if frequency is daily`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Daily,
            )
            val dateCompleted: LocalDate = LocalDate.of(2026, 7, 10)
            val expectedScheduledDate = LocalDate.of(2026, 7, 11)

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.scheduledDate assertIsEqualTo expectedScheduledDate
                },
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 7 days from completed date if frequency is weekly`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Weekly,
            )

            val dateCompleted: LocalDate = LocalDate.of(2026, 7, 10)
            val expectedScheduledDate = LocalDate.of(2026, 7, 17)

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.scheduledDate assertIsEqualTo expectedScheduledDate
                },
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 14 days from completed date if frequency is fortnightly`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Fortnightly,
            )

            val dateCompleted: LocalDate = LocalDate.of(2026, 7, 10)
            val expectedScheduledDate = LocalDate.of(2026, 7, 24)

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.scheduledDate assertIsEqualTo expectedScheduledDate
                },
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 1 month from completed date if frequency is monthly`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Monthly,
            )

            val dateCompleted: LocalDate = LocalDate.of(2026, 7, 10)
            val expectedScheduledDate = LocalDate.of(2026, 8, 10)

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.scheduledDate assertIsEqualTo expectedScheduledDate
                },
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 3 months from completed date if frequency is quarterly`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Quarterly,
            )

            val dateCompleted: LocalDate = LocalDate.of(2026, 7, 10)
            val expectedScheduledDate = LocalDate.of(2026, 10, 10)

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.scheduledDate assertIsEqualTo expectedScheduledDate
                },
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 6 months from completed date if frequency is bi-anually`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.BiAnnually,
            )

            val dateCompleted: LocalDate = LocalDate.of(2026, 7, 10)
            val expectedScheduledDate = LocalDate.of(2027, 1, 10)

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.scheduledDate assertIsEqualTo expectedScheduledDate
                },
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 1 year from completed date if frequency is annually`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Annually,
            )

            val dateCompleted: LocalDate = LocalDate.of(2026, 7, 10)
            val expectedScheduledDate = LocalDate.of(2027, 7, 10)

            taskEditor(
                tasks = setOf(task()),
                updateTask = { reassignedTask ->
                    reassignedTask.scheduledDate assertIsEqualTo expectedScheduledDate
                },
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
        }
    }

    @EnumSource(Frequency::class)
    @ParameterizedTest
    fun `reassignTask() - ByScheduledDate filter is passed into flowOfTasks`(frequency: Frequency) {
        runTest {
            val dateCompleted = LocalDate.of(2026, 6, 14)

            TaskEditorImpl(
                flowOfTasks = { filter ->
                    filter assertIsEqualTo TaskFilter.ByScheduledDate(
                        startDateInclusive = dateCompleted,
                        endDateInclusive = when (frequency) {
                            Frequency.Daily -> LocalDate.of(2026, 6, 15)
                            Frequency.Weekly -> LocalDate.of(2026, 6, 21)
                            Frequency.Fortnightly -> LocalDate.of(2026, 6, 28)
                            Frequency.Monthly -> LocalDate.of(2026, 7, 14)
                            Frequency.Quarterly -> LocalDate.of(2026, 9, 14)
                            Frequency.BiAnnually -> LocalDate.of(2026, 12, 14)
                            Frequency.Annually -> LocalDate.of(2027, 6, 14)
                        }
                    )
                    flowOf(setOf(task()))
                },
                flowOfAllResidents = { flowOf(allResidents) },
                updateTask = { _ -> },
                createTask = { _ -> },
            )
        }
    }

    @Test
    fun `reassignTask() - assign task to resident with the least workload`() {
        runTest {
            val originalTask = task(assignedTo = Resident.One.value)
            val dateCompleted = LocalDate.of(2026, 7, 25)

            // Resident 1 workload is 2 hours 20 minutes
            val allTasksForResident1 = setOf(
                task(
                    duration = 20.minutes,
                    assignedTo = Resident.One.value,
                ),
                task(
                    duration = 2.hours,
                    assignedTo = Resident.One.value,
                )
            )

            // Resident 2 workload is 30 minutes
            val allTasksForResident2 = setOf(
                task(
                    duration = 30.minutes,
                    assignedTo = Resident.Two.value,
                ),
            )

            // Resident 3 workload is 6 hours
            val allTasksForResident3 = setOf(
                task(
                    duration = 4.hours,
                    assignedTo = Resident.Three.value,
                ),
                task(
                    duration = 2.hours,
                    assignedTo = Resident.Three.value,
                )
            )

            val allTasks = allTasksForResident1 + allTasksForResident2 + allTasksForResident3
            val expectedAssignedTo = Resident.Two.value

            taskEditor(
                tasks = allTasks,
                allResidents = allResidents,
                updateTask = { reassignedTask ->
                    reassignedTask.assigneeId assertIsEqualTo expectedAssignedTo
                }
            )
                .reassignTask(
                    task = originalTask,
                    dateCompleted = dateCompleted,
                )
        }
    }

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

    private val allResidents: Set<ResidentDomain> = setOf(
        Resident.One.value,
        Resident.Two.value,
        Resident.Three.value,
    )

    private val dateCompleted: LocalDate = LocalDate.of(2026, 7, 10)

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
        assigneeId = assignedTo,
    )

    private fun taskEditor(
        tasks: Set<Task> = emptySet(),
        allResidents: Set<ResidentDomain> = emptySet(),
        updateTask: UpdateTask,
        createTask: CreateTask = CreateTask { _ -> },
    ): TaskEditorImpl {
        return TaskEditorImpl(
            flowOfTasks = { _ -> flowOf(tasks) },
            flowOfAllResidents = { flowOf(allResidents) },
            updateTask = updateTask,
            createTask = createTask,
        )
    }
}