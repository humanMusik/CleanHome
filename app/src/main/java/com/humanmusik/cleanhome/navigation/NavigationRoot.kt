package com.humanmusik.cleanhome.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDateFreqUrgencyScreen
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDateFreqUrgencyViewModel
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDurationScreen
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDurationViewModel
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationNameRoomScreen
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskParcelData
import com.humanmusik.cleanhome.presentation.tasklist.TaskListScreen
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data object TaskListNavKey : NavKey

@Serializable
data class TaskDetailsNavKey(@Contextual val task: Task) : NavKey

@Serializable
sealed interface TaskCreationNavKey : NavKey {
    @Serializable
    data object NameRoom : TaskCreationNavKey

    @Serializable
    data class DateFrequencyUrgency(
        @Contextual val taskParcelData: TaskParcelData,
    ) :
        TaskCreationNavKey

    @Serializable
    data class Duration(
        @Contextual val taskParcelData: TaskParcelData,
    ) : TaskCreationNavKey
}

@Composable
fun NavigationRoot(modifier: Modifier) {
    val backStack = rememberNavBackStack(TaskListNavKey)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<TaskListNavKey> {
                TaskListScreen(navigation = { it.provideInstructions(backStack) })
            }

            entry<TaskCreationNavKey.NameRoom> {
                TaskCreationNameRoomScreen(navigation = { it.provideInstructions(backStack) })
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
                    navigation = { it.provideInstructions(backStack) }
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
                    navigation = { it.provideInstructions(backStack) }
                )
            }
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
    )
}