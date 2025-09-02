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
    val id: Id?,
    val name: String?,
    val roomId: Room.Id?,
    val duration: Duration?,
    val frequency: Frequency?,
    val scheduledDate: LocalDate?,
    val urgency: Urgency?,
    val assigneeId: Resident.Id?,
) : Comparable<Task>, Parcelable {

    @JvmInline
    @Parcelize
    value class Id(val value: Int) : Parcelable

    override fun compareTo(other: Task): Int =
        taskComparator.compare(this, other)

    companion object {
        fun build(
            id: Id? = null,
            name: String? = null,
            roomId: Room.Id? = null,
            duration: Duration? = null,
            frequency: Frequency? = null,
            scheduledDate: LocalDate? = null,
            urgency: Urgency? = null,
            assigneeId: Resident.Id? = null,
        ) = Task(
            id = id,
            name = name,
            roomId = roomId,
            duration = duration,
            frequency = frequency,
            scheduledDate = scheduledDate,
            urgency = urgency,
            assigneeId = assigneeId,
        )
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

fun String.toFrequency(): Frequency? = Frequency.entries.firstOrNull { it.name == this }
