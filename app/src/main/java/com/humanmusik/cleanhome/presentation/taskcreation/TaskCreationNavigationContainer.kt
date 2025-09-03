package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavBackStack
import com.humanmusik.cleanhome.navigation.TaskCreationNavKey

//class TaskCreationNavigationContainer(
//    private val viewModel: ViewModel,
//    private val backStack: NavBackStack,
//    private val key: TaskCreationNavKey,
//) {
//    @Composable
//    fun NavigateToScreen() {
//        when (key) {
//            TaskCreationNavKey.NameRoom -> {
//                TaskCreationNameRoomScreen(
//                    viewModel = viewModel as TaskCreationNameRoomViewModel,
//                    onContinue = { taskParcelData ->
//                        backStack.add(TaskCreationNavKey.DateFrequencyUrgency(taskParcelData))
//                    }
//                )
//            }
//            else -> throw RuntimeException("Invalid NavKey")
////            TaskCreationNavKey.DataFreqUrgency -> {
////                val viewModel =
////                    hiltViewModel<TaskDetailsViewModel, TaskDetailsViewModel.Factory>(
////                        creationCallback = { factory ->
////                            factory.create(key)
////                        }
////                    )
////                TaskDetailsScreen(viewModel = viewModel)
////            }
////            TaskCreationNavKey.Duration -> TODO()
//        }
//    }
//}
