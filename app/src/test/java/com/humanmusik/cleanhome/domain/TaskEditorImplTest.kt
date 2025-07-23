package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskEditorImpl
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.utilstest.assertIsEqualTo
import com.humanmusik.cleanhome.utilstest.runTest
import com.humanmusik.cleanhome.utilstest.test
import kotlinx.coroutines.flow.Flow
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

            taskEditor(
                tasks = setOf(task()),
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().id assertIsEqualTo 1
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - reassigned task has the same name`() {
        runTest {
            val originalTask = task(name = "Vacuum")

            taskEditor(
                tasks = setOf(task())
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().name assertIsEqualTo "Vacuum"
                    awaitComplete()
                }
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

            taskEditor(setOf(task()))
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().room assertIsEqualTo
                            Room(
                                id = 1,
                                name = "Living Room",
                                homeId = 1,
                            )
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - reassigned task has the same duration`() {
        runTest {
            val originalTask = task(duration = 1.hours)

            taskEditor(setOf(task()))
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().duration assertIsEqualTo 1.hours
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - reassigned task has the same urgency`() {
        runTest {
            val originalTask = task(urgency = Urgency.Urgent)

            taskEditor(setOf(task()))
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().urgency assertIsEqualTo Urgency.Urgent
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 1 day from completed date if frequency is daily`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Daily,
            )

            taskEditor(setOf(task()))
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().scheduledDate assertIsEqualTo
                            LocalDate.of(2026, 7, 11)
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 7 days from completed date if frequency is weekly`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Weekly,
            )

            taskEditor(setOf(task()))
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().scheduledDate assertIsEqualTo
                            LocalDate.of(2026, 7, 17)
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 14 days from completed date if frequency is fortnightly`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Fortnightly,
            )

            taskEditor(setOf(task()))
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().scheduledDate assertIsEqualTo
                            LocalDate.of(2026, 7, 24)
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 1 month from completed date if frequency is monthly`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Monthly,
            )

            taskEditor(setOf(task()))
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().scheduledDate assertIsEqualTo
                        LocalDate.of(2026, 8, 10)
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 3 months from completed date if frequency is quarterly`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Quarterly,
            )

            taskEditor(setOf(task()))
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().scheduledDate assertIsEqualTo
                            LocalDate.of(2026, 10, 10)
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 6 months from completed date if frequency is bi-anually`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.BiAnnually,
            )

            taskEditor(allResidents = allResidents)
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().scheduledDate assertIsEqualTo
                            LocalDate.of(2027, 1, 10)
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - scheduled date is 1 year from completed date if frequency is annually`() {
        runTest {
            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 7, 7),
                frequency = Frequency.Annually,
            )

            taskEditor(allResidents = allResidents)
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().scheduledDate assertIsEqualTo
                            LocalDate.of(2027, 7, 10)
                    awaitComplete()
                }
        }
    }

    @EnumSource(Frequency::class)
    @ParameterizedTest
    fun `reassignTask() - ByScheduledDate filter is passed into flowOfTasks`(frequency: Frequency) {
        runTest {
            val originalTask = task()
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
                flowOfAllResidents = { flowOf(allResidents) }
            )
        }
    }

//    @Test
//    fun `reassignTask() - `() {}

    @Test
    fun `reassignTask() - assign task to resident with least workload in next 1 day if frequency is daily`() {
        runTest {

            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 6, 6),
                frequency = Frequency.Daily,
            )

            val dateCompleted = LocalDate.of(2026, 6, 14)

            // Resident 3 workload in next 1 day is 2 hours 20 minutes
            val allTasksForResident1 = setOf(
                task(
                    scheduledDate = LocalDate.of(2026, 6, 15),
                    frequency = Frequency.Weekly,
                    duration = 20.minutes,
                    assignedTo = Resident.One.value,
                ),
                task(
                    scheduledDate = LocalDate.of(2026, 6, 15),
                    frequency = Frequency.BiAnnually,
                    duration = 2.hours,
                    assignedTo = Resident.One.value,
                )
            )

            // Resident 3 workload in next 1 day is 30 minutes
            val allTasksForResident2 = setOf(
                task(
                    scheduledDate = LocalDate.of(2026, 6, 15),
                    frequency = Frequency.Weekly,
                    duration = 30.minutes,
                    assignedTo = Resident.Two.value,
                ),
            )

            // Resident 3 workload in next 1 day is 6 hours
            val allTasksForResident3 = setOf(
                task(
                    scheduledDate = LocalDate.of(2026, 6, 15),
                    frequency = Frequency.Annually,
                    duration = 4.hours,
                    assignedTo = Resident.Three.value,
                ),
                task(
                    scheduledDate = LocalDate.of(2026, 6, 15),
                    frequency = Frequency.Monthly,
                    duration = 2.hours,
                    assignedTo = Resident.Three.value,
                )
            )

            val allTasks = allTasksForResident1 + allTasksForResident2 + allTasksForResident3

            val residentWithLeastWorkload = Resident.Two.value

            taskEditor(
                allResidents = allResidents,
                tasks = allTasks,
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().assignedTo assertIsEqualTo residentWithLeastWorkload
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - assign task to resident with least workload in next 7 days if frequency is weekly`() {
        runTest {

            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 6, 6),
                frequency = Frequency.Weekly,
            )

            val dateCompleted = LocalDate.of(2026, 6, 14)

            // Resident 1 workload in next 7 days is 4 hours 20 minutes
            val allTasksForResident1 = setOf(
                task(
                    scheduledDate = LocalDate.of(2026, 6, 20),
                    duration = 20.minutes,
                    assignedTo = Resident.One.value,
                ),
                task(
                    scheduledDate = LocalDate.of(2026, 7, 18),
                    duration = 2.hours,
                    assignedTo = Resident.One.value,
                ),
                task(
                    scheduledDate = LocalDate.of(2026, 6, 13),
                    duration = 2.hours,
                    assignedTo = Resident.One.value,
                )
            )

            // Resident 2 workload in next 7 days is 0
            val allTasksForResident2 = emptySet<Task>()

            // Resident 3 workload in next 7 days is 6 hours
            val allTasksForResident3 = setOf(
                task(
                    scheduledDate = LocalDate.of(2026, 6, 7),
                    duration = 4.hours,
                    assignedTo = Resident.Three.value,
                ),
                task(
                    scheduledDate = LocalDate.of(2026, 6, 21),
                    duration = 2.hours,
                    assignedTo = Resident.Three.value,
                )
            )

            val allTasks = allTasksForResident1 + allTasksForResident2 + allTasksForResident3
            val residentWithLeastWorkload = Resident.One.value

            taskEditor(
                allResidents = allResidents,
                tasks = allTasks,
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().assignedTo assertIsEqualTo residentWithLeastWorkload
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - assign task to resident with least workload in next 14 days if frequency is fortnightly`() {
        runTest {

            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 6, 6),
                frequency = Frequency.Fortnightly,
            )

            val dateCompleted = LocalDate.of(2026, 6, 14)

            // Resident 1 workload in next 14 days is 0
            val allTasksForResident1 = emptySet<Task>()

            // Resident 2 workload in next 14 days is 30 minutes
            val allTasksForResident2 = setOf(
                task(
                    scheduledDate = LocalDate.of(2026, 6, 27),
                    duration = 30.minutes,
                    assignedTo = Resident.Two.value,
                ),
            )

            // Resident 3 workload in next 14 days is 6 hours
            val allTasksForResident3 = listOf(
                task(
                    scheduledDate = LocalDate.of(2026, 6, 20),
                    duration = 4.hours,
                    assignedTo = Resident.Three.value,
                ),
                task(
                    scheduledDate = LocalDate.of(2026, 6, 28),
                    duration = 2.hours,
                    assignedTo = Resident.Three.value,
                )
            )

            val allTasks = allTasksForResident1 + allTasksForResident2 + allTasksForResident3
            val residentWithLeastWorkload = Resident.One.value

            taskEditor(
                allResidents = allResidents,
                tasks = allTasks,
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().assignedTo assertIsEqualTo residentWithLeastWorkload
                    awaitComplete()
                }
        }
    }

    @Test
    fun `reassignTask() - assign task to resident with least workload in next month if frequency is monthly`() {
        runTest {

            val originalTask = task(
                scheduledDate = LocalDate.of(2026, 6, 6),
                frequency = Frequency.Monthly,
            )

            val dateCompleted = LocalDate.of(2026, 6, 14)

            // Resident 1 workload in next 1 month is 20 minutes
            val allTasksForResident1 = setOf(
                task(
                    scheduledDate = LocalDate.of(2026, 7, 7),
                    duration = 20.minutes,
                    assignedTo = Resident.One.value,
                ),
                task(
                    scheduledDate = LocalDate.of(2026, 8, 7),
                    duration = 2.hours,
                    assignedTo = Resident.One.value,
                ),
                task(
                    scheduledDate = LocalDate.of(2026, 6, 11),
                    duration = 2.hours,
                    assignedTo = Resident.One.value,
                )
            )

            // Resident 2 workload in next 1 month is 30 minutes
            val allTasksForResident2 = setOf(
                task(
                    scheduledDate = LocalDate.of(2026, 6, 27),
                    duration = 30.minutes,
                    assignedTo = Resident.Two.value,
                ),
            )

            // Resident 3 workload in next 1 month is 0
            val allTasksForResident3 = setOf(
                task(
                    scheduledDate = LocalDate.of(2026, 7, 20),
                    duration = 4.hours,
                    assignedTo = Resident.Three.value,
                ),
                task(
                    scheduledDate = LocalDate.of(2026, 7, 28),
                    duration = 2.hours,
                    assignedTo = Resident.Three.value,
                )
            )

            val allTasks = allTasksForResident1 + allTasksForResident2 + allTasksForResident3
            val residentWithLeastWorkload = Resident.Three.value

            taskEditor(
                allResidents = allResidents,
                tasks = allTasks,
            )
                .reassignTask(task = originalTask, dateCompleted = dateCompleted)
                .test {
                    awaitItem().assignedTo assertIsEqualTo residentWithLeastWorkload
                    awaitComplete()
                }
        }
    }

//    @Test
//    fun `reassignTask() - assign task to resident with least workload in 3 months if frequency is quarterly`() {
//        val originalTask = task(
//            scheduledDate = LocalDate.of(2026, 6, 6),
//            frequency = Frequency.Quarterly,
//        )
//
//        val dateCompleted = LocalDate.of(2026, 6, 14)
//
//        val allTasksForResident1 = listOf(
//            task(
//                scheduledDate = LocalDate.of(2026, 11, 7),
//                duration = 20.minutes,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2026, 10, 7),
//                duration = 2.hours,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2026, 7, 11),
//                duration = 2.hours,
//            )
//        )
//        val allTasksForResident2 = listOf(
//            task(
//                scheduledDate = LocalDate.of(2026, 9, 27),
//                duration = 30.minutes,
//            ),
//        )
//        val allTasksForResident3 = listOf(
//            task(
//                scheduledDate = LocalDate.of(2026, 7, 10),
//                duration = 4.hours,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2027, 1, 4),
//                duration = 2.hours,
//            )
//        )
//
//        val residentWithLeastWorkload = Resident.Two.value
//
//        val mapOfResidentIdToTasks = taskMapper(
//            1 to flowOf(allTasksForResident1),
//            2 to flowOf(allTasksForResident2),
//            3 to flowOf(allTasksForResident3),
//        )
//
//        taskEditor(
//            allResidents = allResidents,
//            tasksForResidentMapper = mapOfResidentIdToTasks,
//        )
//            .reassignTask(task = originalTask, dateCompleted = dateCompleted)
//            .assignedTo assertIsEqualTo residentWithLeastWorkload
//    }
//
//    @Test
//    fun `reassignTask() - assign task to resident with least workload in 6 months if frequency is biannually`() {
//        val originalTask = task(
//            scheduledDate = LocalDate.of(2026, 6, 6),
//            frequency = Frequency.BiAnnually,
//        )
//
//        val dateCompleted = LocalDate.of(2026, 7, 14)
//
//        val allTasksForResident1 = listOf(
//            task(
//                scheduledDate = LocalDate.of(2027, 11, 7),
//                duration = 20.minutes,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2026, 10, 7),
//                duration = 2.hours,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2026, 7, 11),
//                duration = 2.hours,
//            )
//        )
//        val allTasksForResident2 = listOf(
//            task(
//                scheduledDate = LocalDate.of(2026, 9, 27),
//                duration = 30.minutes,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2026, 1, 13),
//                duration = 6.hours,
//            ),
//        )
//        val allTasksForResident3 = listOf(
//            task(
//                scheduledDate = LocalDate.of(2027, 2, 10),
//                duration = 4.hours,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2027, 1, 15),
//                duration = 2.hours,
//            )
//        )
//
//        val residentWithLeastWorkload = Resident.Three.value
//
//        val mapOfResidentIdToTasks = taskMapper(
//            1 to flowOf(allTasksForResident1),
//            2 to flowOf(allTasksForResident2),
//            3 to flowOf(allTasksForResident3),
//        )
//
//        taskEditor(
//            allResidents = allResidents,
//            tasksForResidentMapper = mapOfResidentIdToTasks,
//        )
//            .reassignTask(task = originalTask, dateCompleted = dateCompleted)
//            .assignedTo assertIsEqualTo residentWithLeastWorkload
//    }
//
//    @Test
//    fun `reassignTask() - assign task to resident with least workload in next 1 year if frequency is annually`() {
//        val originalTask = task(
//            scheduledDate = LocalDate.of(2026, 6, 6),
//            frequency = Frequency.Annually,
//        )
//
//        val dateCompleted = LocalDate.of(2026, 7, 14)
//
//        val allTasksForResident1 = listOf(
//            task(
//                scheduledDate = LocalDate.of(2027, 1, 15),
//                duration = 20.minutes,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2027, 7, 15),
//                duration = 2.hours,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2027, 12, 11),
//                duration = 2.hours,
//            )
//        )
//        val allTasksForResident2 = listOf(
//            task(
//                scheduledDate = LocalDate.of(2026, 9, 27),
//                duration = 30.minutes,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2026, 1, 13),
//                duration = 6.hours,
//            ),
//        )
//        val allTasksForResident3 = listOf(
//            task(
//                scheduledDate = LocalDate.of(2028, 6, 10),
//                duration = 4.hours,
//            ),
//            task(
//                scheduledDate = LocalDate.of(2027, 1, 15),
//                duration = 2.hours,
//            )
//        )
//
//        val residentWithLeastWorkload = Resident.One.value
//
//        val mapOfResidentIdToTasks = taskMapper(
//            1 to flowOf(allTasksForResident1),
//            2 to flowOf(allTasksForResident2),
//            3 to flowOf(allTasksForResident3),
//        )
//
//        taskEditor(
//            allResidents = allResidents,
//            tasksForResidentMapper = mapOfResidentIdToTasks,
//        )
//            .reassignTask(task = originalTask, dateCompleted = dateCompleted).assignedTo assertIsEqualTo residentWithLeastWorkload
//    }
//
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
        assignedTo = assignedTo,
    )

    private fun taskEditor(
        tasks: Set<Task> = emptySet(),
        allResidents: Set<ResidentDomain> = emptySet(),
    ): TaskEditorImpl {
        return TaskEditorImpl(
            flowOfTasks = { _ -> flowOf(tasks) },
            flowOfAllResidents = { flowOf(allResidents) },
        )
    }

    private fun taskMapper(vararg inputs: Pair<Int, Flow<List<Task>>>): Int.() -> Flow<List<Task>> {
        val outputByReceiver: Map<Int, Flow<List<Task>>> = inputs.toMap()
        return {
            outputByReceiver[this] ?: throw IllegalStateException("Unexpected result $this")
        }
    }
}