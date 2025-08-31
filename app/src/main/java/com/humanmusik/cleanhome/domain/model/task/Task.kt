package com.humanmusik.cleanhome.domain.model.task

import android.os.Parcelable
import com.humanmusik.cleanhome.domain.model.ResidentId
import com.humanmusik.cleanhome.domain.model.RoomId
import com.humanmusik.cleanhome.domain.taskComparator
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.Contract
import java.time.LocalDate
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.time.Duration

@Parcelize
data class Task(
    val id: TaskId,
    val name: String?,
    val roomId: RoomId,
    val duration: Duration?,
    val frequency: Frequency?,
    val scheduledDate: LocalDate?,
    val urgency: Urgency?,
    val assigneeId: ResidentId,
) : Comparable<Task>, Parcelable {
    override fun compareTo(other: Task): Int =
        taskComparator.compare(this, other)

    companion object {
        fun build(
            id: TaskId = TaskId(null),
            name: String? = null,
            roomId: RoomId = RoomId(null),
            duration: Duration? = null,
            frequency: Frequency? = null,
            scheduledDate: LocalDate? = null,
            urgency: Urgency? = null,
            assigneeId: ResidentId = ResidentId(null),
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

@JvmInline
@Parcelize
value class TaskId(val value: Int?) : Parcelable

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

