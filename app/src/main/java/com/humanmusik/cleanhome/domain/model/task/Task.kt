package com.humanmusik.cleanhome.domain.model.task

import android.os.Parcelable
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.taskComparator
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.Duration
import kotlin.jvm.Throws

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
    val state: State?,
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
            state: State? = null,
        ) = Task(
            id = id,
            name = name,
            roomId = roomId,
            duration = duration,
            frequency = frequency,
            scheduledDate = scheduledDate,
            urgency = urgency,
            assigneeId = assigneeId,
            state = state,
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
    ;

    fun isUrgent(): Boolean = this == Urgent
}

enum class State {
    Active,
    Inactive,
    Unrecognised,
}

fun Boolean.toUrgency() = if (this) Urgency.Urgent else Urgency.NonUrgent

@Throws(IllegalStateException::class)
fun String.toFrequencyOrThrow(): Frequency =
    Frequency.entries.firstOrNull { it.name == this } ?: throw IllegalStateException()
