package com.humanmusik.cleanhome.domain.model.task

import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import java.time.OffsetDateTime

data class Task(
    val id: Int,
    val name: String,
    val room: Room,
    val duration: Long,
    val frequency: Frequency,
    val scheduledDate: OffsetDateTime,
    val urgency: Urgency,
    val assignedTo: Resident,
)

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
