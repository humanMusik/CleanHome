package com.humanmusik.cleanhome.navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDateFreqUrgencyScreen
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDateFreqUrgencyViewModel
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDurationScreen
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDurationViewModel
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationNameRoomScreen
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskCreationParcelData
import com.humanmusik.cleanhome.presentation.tasklist.TaskListScreen
import kotlinx.parcelize.Parcelize

interface CustomNavKey : Parcelable

@Parcelize
data class BackStack(val navKeys: SnapshotStateList<CustomNavKey>) : Parcelable

@Parcelize
data object TaskListNavKey : CustomNavKey

@Parcelize
data class TaskDetailsNavKey(val taskId: Task.Id) : CustomNavKey

@Parcelize
sealed interface TaskCreationNavKey : CustomNavKey {
    data object NameRoom : TaskCreationNavKey

    data class DateFrequencyUrgency(
        val taskCreationParcelData: TaskCreationParcelData,
    ) : TaskCreationNavKey

    data class Duration(
        val taskCreationParcelData: TaskCreationParcelData,
    ) : TaskCreationNavKey
}

@Composable
fun NavigationRoot(
    modifier: Modifier,
) {
    val viewModel: NavigationViewModel = hiltViewModel()
    val backStack = viewModel.backStack.collectAsState().value.navKeys

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        onBack = { viewModel.pop() },
        entryProvider = entryProvider {
            entry<TaskListNavKey> {
                TaskListScreen(
                    onAddTaskNavigator = { viewModel.push(TaskCreationNavKey.NameRoom) },
                    onTaskSelectedNavigator = { taskId -> viewModel.push(TaskDetailsNavKey(taskId = taskId)) }
                )
            }

            entry<TaskCreationNavKey.NameRoom> {
                TaskCreationNameRoomScreen(
                    onContinueNavigation = { taskParcelData ->
                        viewModel.push(TaskCreationNavKey.DateFrequencyUrgency(taskParcelData))
                    },
                )
            }

            entry<TaskCreationNavKey.DateFrequencyUrgency> { key ->
                val taskCreationDateFreqUrgencyViewModel: TaskCreationDateFreqUrgencyViewModel =
                    hiltViewModel<TaskCreationDateFreqUrgencyViewModel, TaskCreationDateFreqUrgencyViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        }
                    )

                TaskCreationDateFreqUrgencyScreen(
                    viewModel = taskCreationDateFreqUrgencyViewModel,
                    onContinueNavigation = { taskParcelData ->
                        viewModel.push(TaskCreationNavKey.Duration(taskParcelData))
                    },
                )
            }

            entry<TaskCreationNavKey.Duration> { key ->
                val taskCreationDurationViewModel: TaskCreationDurationViewModel =
                    hiltViewModel<TaskCreationDurationViewModel, TaskCreationDurationViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        }
                    )

                TaskCreationDurationScreen(
                    viewModel = taskCreationDurationViewModel,
                    onCreateTaskNavigation = { viewModel.popUntil(TaskListNavKey) },
                )
            }


//            { key ->
//            when (key) {
//                is TaskListNavKey -> {
//                    NavEntry(key = key) {
//                        val taskListViewModel: TaskListViewModel = hiltViewModel()
//
//                        TaskListScreen(
//                            viewModel = taskListViewModel,
//                            onExamine = { task ->
//                                backStack.add(TaskDetailsNavKey(task))
//                            },
//                            onAddTask = {
////                                println("Les: NavRoot ${it.instructions}")
////                                it.provideInstructions(backStack)
//                                backStack.add(TaskCreationNameRoomNavKey)
//                            }
//                        )
//                    }
//                }
//
//                is TaskDetailsNavKey -> {
//                    NavEntry(key = key) {
//                        val viewModel =
//                            hiltViewModel<TaskDetailsViewModel, TaskDetailsViewModel.Factory>(
//                                creationCallback = { factory ->
//                                    factory.create(key)
//                                }
//                            )
//                        TaskDetailsScreen(viewModel = viewModel)
//                    }
//                }
//
//                is TaskCreationNameRoomNavKey -> {
//                    NavEntry(key = key) {
////                        val taskCreationViewModel = when (key) {
////                            TaskCreationNavKey.NameRoom -> hiltViewModel<TaskCreationNameRoomViewModel>()
////                            is TaskCreationNavKey.DateFrequencyUrgency -> hiltViewModel<TaskCreationDateFreqUrgencyViewModel>()
////                            TaskCreationNavKey.Duration -> hiltViewModel<TaskCreationDurationViewModel>()
////                        }
////                        TaskCreationNavigationContainer(
////                            viewModel = taskCreationViewModel,
////                            backStack = backStack,
////                            key = key,
////                        )
////                            .NavigateToScreen()
//                        val taskCreationNameRoomViewModel: TaskCreationNameRoomViewModel = hiltViewModel()
//                        TaskCreationNameRoomScreen(
//                            viewModel = taskCreationNameRoomViewModel,
//                            onContinue = { }
//                        )
//                    }
//                }
//
//                else -> throw RuntimeException("Invalid NavKey")
//            }
//        }

        }
    )
}