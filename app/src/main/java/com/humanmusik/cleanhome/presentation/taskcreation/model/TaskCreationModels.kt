package com.humanmusik.cleanhome.presentation.taskcreation.model

import android.os.Parcelable
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.State
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.Urgency
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.Duration

@Parcelize
data class TaskCreationParcelData(
    val name: String? = null,
    val room: Room.Id? = null,
    val date: LocalDate? = null,
    val frequency: Frequency? = null,
    val urgency: Urgency? = null,
    val duration: Duration? = null,
) : Parcelable {
    fun createTask(assigneeId: Resident.Id): Task =
        Task(
            id = null,
            name = requireNotNull(name) { "Task name cannot be null" },
            roomId = requireNotNull(room) { "Task roomId cannot be null" },
            scheduledDate = requireNotNull(date) { "Task scheduledDate cannot be null" },
            frequency = requireNotNull(frequency) { "Task frequency cannot be null" },
            urgency = requireNotNull(urgency) { "Task urgency cannot be null" },
            duration = requireNotNull(duration) { "Task duration cannot be null" },
            assigneeId = assigneeId,
            state = State.Active,
            lastCompletedDate = null,
        )
}

@Parcelize
data class TaskCreationNameRoomState(
    val allRooms: List<Room>,
) : Parcelable