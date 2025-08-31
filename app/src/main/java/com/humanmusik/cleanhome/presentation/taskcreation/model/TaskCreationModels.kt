package com.humanmusik.cleanhome.presentation.taskcreation.model

import android.os.Parcelable
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.Urgency
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import kotlin.time.Duration

@Parcelize
data class TaskParcelData(
    val name: String? = null,
    val room: Room? = null,
    val date: LocalDate? = null,
    val frequency: Frequency? = null,
    val urgency: Urgency? = null,
    val duration: Duration? = null,
) : Parcelable {
    fun hasNullProperties() = name == null || room == null || date == null || frequency == null
            || urgency == null || duration == null

    fun toTask(assignedTo: Resident): Task {
        return if (hasNullProperties()) {
            throw IllegalStateException()
        } else {
            Task(
                id = null,
                name = name!!,
                room = room!!,
                scheduledDate = date!!,
                frequency = frequency!!,
                urgency = urgency!!,
                duration = duration!!,
                assigneeId = assignedTo,
            )
        }
    }
}

@Parcelize
data class TaskCreationNameRoomState(
    val allRooms: List<Room>,
) : Parcelable