package com.humanmusik.cleanhome.domain.model.task

import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import java.time.LocalDate
import java.time.LocalDateTime

data class Task(
    val id: Int,
    val name: String,
    val room: Room,
    val duration: Long,
    val frequency: Frequency,
    val scheduledDate: LocalDate,
    val urgency: Urgency,
    val assignedTo: Resident,
) {
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
