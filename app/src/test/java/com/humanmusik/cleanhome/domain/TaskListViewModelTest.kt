package com.humanmusik.cleanhome.domain

import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.data.repository.FlowOfTasks
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.getOrNull
import com.humanmusik.cleanhome.presentation.tasklist.TaskListModel
import com.humanmusik.cleanhome.presentation.tasklist.TaskListViewModel
import com.humanmusik.cleanhome.utilstest.assertIsEqualTo
import com.humanmusik.cleanhome.utilstest.assertIsInstanceOf
import com.humanmusik.cleanhome.utilstest.runCancellingTest
import com.humanmusik.cleanhome.utilstest.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class TaskListViewModelTest {

    @Test
    fun `viewModel initialises - flowOfTasks maps to state property tasks`() {
        runCancellingTest {
            val tasks = setOf(
                task(id = 1),
                task(id = 2),
                task(id = 3),
                task(id = 4),
                task(id = 5),
            )

            val viewModel = createViewModel(
                flowOfTasks = { filter ->
                    filter assertIsEqualTo TaskFilter.All
                    flowOf(tasks)
                },
            )

            viewModel.stateFlow.test {
                awaitItem().assertIsInstanceOf<FlowState.Loading<TaskListModel>>()
                awaitItem().getOrNull()?.tasks assertIsEqualTo tasks.toList()
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onCompleteTask() - state property tasks removes completed task`() {
        runCancellingTest {
            val tasks = setOf(
                task(id = 1),
                task(id = 2),
                task(id = 3),
                task(id = 4),
                task(id = 5),
            )

            val completedTask = task(id = 1)

            val viewModel = createViewModel(
                flowOfTasks = { filter ->
                    filter assertIsEqualTo TaskFilter.All
                    flowOf(tasks)
                },
            )

            viewModel.stateFlow.test {
                skipItems(2)

                viewModel.onCompleteTask(completedTask)
                advanceUntilIdle()

                awaitItem().getOrNull().assertIsInstanceOf<FlowState.Success<TaskListModel>> {
                    value.tasks assertIsEqualTo listOf(
                        task(id = 2),
                        task(id = 3),
                        task(id = 4),
                        task(id = 5),
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onCompleteTask() - reassignTask succeeds`() {
        runCancellingTest {
            val tasks = setOf(
                task(id = 1),
                task(id = 2),
                task(id = 3),
                task(id = 4),
                task(id = 5),
            )

            val completedTask = task(id = 1)

            val viewModel = createViewModel(
                flowOfTasks = { filter ->
                    filter assertIsEqualTo TaskFilter.All
                    flowOf(tasks)
                },
                taskEditor = object : TaskEditor {
                    override suspend fun reassignTask(task: Task, dateCompleted: LocalDate) {
                        /* Does not throw */
                    }
                }
            )

            viewModel.stateFlow.test {
                skipItems(2)

                viewModel.onCompleteTask(completedTask)
                advanceUntilIdle()

                awaitItem().getOrNull().assertIsInstanceOf<FlowState.Success<List<Task>>>()
            }
        }
    }

    private fun task(
        id: Int? = 1,
        name: String = "Vacuum",
        room: com.humanmusik.cleanhome.domain.model.Room = com.humanmusik.cleanhome.domain.model.Room(
            id = 1,
            name = "Living Room",
            homeId = 1,
        ),
        duration: Duration = 30.minutes,
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun TestScope.createViewModel(
        flowOfTasks: FlowOfTasks = FlowOfTasks { _ -> flowOf(emptySet()) },
        taskEditor: TaskEditor = object : TaskEditor {
            override suspend fun reassignTask(task: Task, dateCompleted: LocalDate) {}
        },
    ) =
        TaskListViewModel(
            flowOfTasks = flowOfTasks,
            taskEditor = taskEditor,
        )
//            .also { runCurrent() }
}