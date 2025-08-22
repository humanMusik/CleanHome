package com.humanmusik.cleanhome.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationNavigationContainer
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskParcelData
import com.humanmusik.cleanhome.presentation.taskdetails.TaskDetailsScreen
import com.humanmusik.cleanhome.presentation.taskdetails.TaskDetailsViewModel
import com.humanmusik.cleanhome.presentation.tasklist.TaskListScreen
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data object TaskListNavKey : NavKey

@Serializable
data class TaskDetailsNavKey(@Contextual val task: Task) : NavKey

@Serializable
sealed interface TaskCreationNavKey : NavKey {
    data class DataFreqUrgency(val taskParcelData: TaskParcelData) : TaskCreationNavKey
    data object Duration : TaskCreationNavKey
    data object NameRoom : TaskCreationNavKey
}

@Composable
fun NavigationRoot(modifier: Modifier) {
    val backStack = rememberNavBackStack(TaskListNavKey)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator(),
        ),
        entryProvider = { key ->
            when (key) {
                is TaskListNavKey -> {
                    NavEntry(key = key) {
                        TaskListScreen(
                            onExamine = { task ->
                                backStack.add(TaskDetailsNavKey(task))
                            },
                            onAddTask = {
                                println("Les: NameRoomNavKey added")
                                backStack.add(TaskCreationNavKey.NameRoom)
                            }
                        )
                    }
                }

                is TaskDetailsNavKey -> {
                    NavEntry(key = key) {
                        val viewModel =
                            hiltViewModel<TaskDetailsViewModel, TaskDetailsViewModel.Factory>(
                                creationCallback = { factory ->
                                    factory.create(key)
                                }
                            )
                        TaskDetailsScreen(viewModel = viewModel)
                    }
                }

                is TaskCreationNavKey -> {
                    NavEntry(key = key) {
                        TaskCreationNavigationContainer(
                            backStack = backStack,
                            key = key,
                        )
                            .NavigateToScreen()
                    }
                }

                else -> throw RuntimeException("Invalid NavKey")
            }
        }
    )
}