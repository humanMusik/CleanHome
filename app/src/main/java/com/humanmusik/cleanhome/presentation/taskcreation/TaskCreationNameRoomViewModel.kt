package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanmusik.cleanhome.data.repository.FlowOfAllRooms
import com.humanmusik.cleanhome.data.repository.FlowOfAllRooms.Companion.invoke
import com.humanmusik.cleanhome.navigation.BackStackInstruction
import com.humanmusik.cleanhome.navigation.BackStackInstructor
import com.humanmusik.cleanhome.navigation.TaskCreationNavKey
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.getOrThrow
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskCreationNameRoomState
import com.humanmusik.cleanhome.presentation.taskcreation.model.TaskParcelData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TaskCreationNameRoomViewModel @Inject constructor(
    flowOfRooms: FlowOfAllRooms,
    private val backStackInstructor: BackStackInstructor,
) : ViewModel() {
    private var mutableStateFlow: MutableStateFlow<FlowState<TaskCreationNameRoomState>> =
        MutableStateFlow(FlowState.Loading())

    val stateFlow = mutableStateFlow.asStateFlow()

    init {
        flowOfRooms().map { rooms ->
            mutableStateFlow.update {
                FlowState.Success(
                    TaskCreationNameRoomState(
                        allRooms = rooms,
                    )
                )
            }
        }
            .launchIn(viewModelScope)

        println("TaskCreationVM: ${backStackInstructor.instructions}")
    }

    fun onContinue(
        taskName: String,
        roomName: String,
    ): BackStackInstructor {
        val selectedRoom = stateFlow.value.getOrThrow().allRooms.first { it.name == roomName }

        val taskParcelData = TaskParcelData()
            .copy(
                name = taskName,
                room = selectedRoom,
            )

        return backStackInstructor.learnInstructions(
            BackStackInstruction.Push(
                TaskCreationNavKey.DateFrequencyUrgency(
                    taskParcelData = taskParcelData
                )
            )
        )
    }
}
