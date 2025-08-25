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

fun String.toFrequency(): Frequency? = Frequency.entries.firstOrNull { it.name == this }

