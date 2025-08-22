package com.humanmusik.cleanhome.presentation.taskcreation.model

import android.os.Parcelable
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Urgency
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import kotlin.time.Duration

@Parcelize
data class TaskParcelData(
    val name: String? = null,
    val roomName: String? = null,
    val date: LocalDate? = null,
    val frequency: Frequency? = null,
    val urgency: Urgency? = null,
    val duration: Duration? = null,
) : Parcelable

@Parcelize
data class TaskCreationNameRoomState(
    val allRooms: List<Room>,
) : Parcelable