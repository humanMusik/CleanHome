package com.humanmusik.cleanhome.domain.model.task

import android.os.Parcelable
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.taskComparator
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import kotlin.time.Duration

@Parcelize
data class Task(
    val id: Int?,
    val name: String,
    val room: Room,
    val duration: Duration,
    val frequency: Frequency,
    val scheduledDate: LocalDate,
    val urgency: Urgency,
    val assignedTo: Resident,
) : Comparable<Task>, Parcelable {
    override fun compareTo(other: Task): Int =
        taskComparator.compare(this, other)

    fun editName(name: String): Task {
        return copy(name = name)
    }

    fun editRoom(room: Room): Task {
        return copy(room = room)
    }

    fun editDuration(room: Room): Task {
        return copy(duration = duration)
    }

    fun editFrequency(frequency: Frequency): Task {
        return copy(frequency = frequency)
    }

    fun editScheduledDate(scheduledDate: LocalDate): Task {
        return copy(scheduledDate = scheduledDate)
    }

    fun editUrgency(urgency: Urgency): Task {
        return copy(urgency = urgency)
    }

    fun editAssignedTo(assignedTo: Resident): Task {
        return copy(assignedTo = assignedTo)
    }
}

enum class Frequency {
    Daily,
    Weekly,
    Fortnightly,
    Monthly,
    Quarterly,
    BiAnnually,
    Annually,
}

enum class Urgency {
    Urgent,
    NonUrgent,
}
