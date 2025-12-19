package com.humanmusik.cleanhome.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.humanmusik.cleanhome.domain.model.authentication.AuthState
import com.humanmusik.cleanhome.presentation.authentication.LoadingUpScreen
import com.humanmusik.cleanhome.presentation.authentication.login.LoginScreen
import com.humanmusik.cleanhome.presentation.authentication.registration.RegistrationScreen
import com.humanmusik.cleanhome.presentation.authentication.registration.RegistrationSuccessScreen
import com.humanmusik.cleanhome.presentation.home.HomeScreen
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDateFreqUrgencyScreen
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDateFreqUrgencyViewModel
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDurationScreen
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationDurationViewModel
import com.humanmusik.cleanhome.presentation.taskcreation.TaskCreationNameRoomScreen
import com.humanmusik.cleanhome.presentation.taskdetails.TaskDetailsScreen
import com.humanmusik.cleanhome.presentation.taskdetails.TaskDetailsViewModel
import com.humanmusik.cleanhome.presentation.tasklist.TaskListScreen

@Composable
fun NavigationRoot(
    modifier: Modifier,
    userSession: AuthState,
) {
    val viewModel: NavigationViewModel = hiltViewModel()
    val backStack = viewModel.backStack.collectAsState().value.navKeys

    LaunchedEffect(userSession) {
        viewModel.clearBackStack()
        viewModel.insertInitialScreen(userSession)
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        onBack = { viewModel.pop() },
        entryProvider = entryProvider {
            entry<AuthenticationNavKey.LoadingUp> {
                LoadingUpScreen()
            }

            entry<AuthenticationNavKey.Login> {
                LoginScreen(
                    onSignUpNavigation = { viewModel.push(AuthenticationNavKey.Registration) },
                    onSuccessfulLoginNavigation = {
                        viewModel.clearBackStack()
                        viewModel.push(HomeNavKey)
                    },
                )
            }

            entry<AuthenticationNavKey.Registration> {
                RegistrationScreen(
                    onRegisterNavigation = {
                        viewModel.pop()
                        viewModel.push(AuthenticationNavKey.RegistrationSuccess)
                    },
                )
            }

            entry<AuthenticationNavKey.RegistrationSuccess> {
                RegistrationSuccessScreen(
                    onOkNavigation = {
                        viewModel.clearBackStack()
                        viewModel.push(AuthenticationNavKey.Login)
                    }
                )
            }

            entry<HomeNavKey> {
                HomeScreen(
                    onHomeSelectedNavigation = { viewModel.push(TaskListNavKey) }
                )
            }

            entry<TaskListNavKey> {
                TaskListScreen(
                    onAddTaskNavigation = { viewModel.push(TaskCreationNavKey.NameRoom) },
                    onTaskSelectedNavigation = { taskId -> viewModel.push(TaskDetailsNavKey(taskId = taskId)) }
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

            entry<TaskDetailsNavKey> { key ->
                val taskDetailsViewModel: TaskDetailsViewModel =
                    hiltViewModel<TaskDetailsViewModel, TaskDetailsViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        }
                    )

                TaskDetailsScreen(
                    viewModel = taskDetailsViewModel,
                    onSaveNavigation = { viewModel.pop() },
                    onDeleteNavigation = { viewModel.pop() },
                )
            }
        }
    )
}