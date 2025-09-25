package com.humanmusik.cleanhome.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ForeignKey.Companion.SET_NULL
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.humanmusik.cleanhome.data.DurationTypeConverter
import com.humanmusik.cleanhome.data.LocalDateTypeConverter
import com.humanmusik.cleanhome.domain.enrichedTaskComparator
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.State
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.Urgency
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.Duration
import java.time.LocalDate

const val TASK_ENTITY_ROOM_ID = "roomId"
const val TASK_ENTITY_ASSIGNEE_ID = "assigneeId"
const val TASK_ENTITY_ID = "id"

@TypeConverters(
    LocalDateTypeConverter::class,
    DurationTypeConverter::class,
)
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = [ROOM_ENTITY_ID],
            childColumns = [TASK_ENTITY_ROOM_ID],
            onDelete = CASCADE,
            onUpdate = CASCADE,
        ),
        ForeignKey(
            entity = ResidentEntity::class,
            parentColumns = [RESIDENT_ENTITY_ID],
            childColumns = [TASK_ENTITY_ASSIGNEE_ID],
            onDelete = SET_NULL,
            onUpdate = CASCADE,
        )
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(TASK_ENTITY_ID)
    val id: Int = 0,
    val name: String,
    @ColumnInfo(TASK_ENTITY_ROOM_ID)
    val roomId: Int,
    val duration: Duration,
    val frequency: Frequency,
    val scheduledDate: LocalDate,
    val urgency: Urgency,
    @ColumnInfo(TASK_ENTITY_ASSIGNEE_ID)
    val assigneeId: Int?,
    val state: State,
)

@TypeConverters(
    LocalDateTypeConverter::class,
    DurationTypeConverter::class,
)
@DatabaseView(
    """
        SELECT taskentity.id AS idInt,
        taskentity.name AS taskName,
        taskentity.duration AS duration,
        taskentity.frequency AS frequency,
        taskentity.scheduledDate AS scheduledDate,
        taskentity.urgency AS urgency,
        taskentity.state AS state,
        roomentity.id AS roomIdInt,
        roomentity.name AS roomName,
        residententity.id AS assigneeIdInt,
        residententity.name AS assigneeName
        FROM taskentity
        INNER JOIN roomentity on taskentity.roomId = roomentity.id
        INNER JOIN residententity on taskentity.assigneeId = residententity.id
    """,
)
@Parcelize
data class EnrichedTaskEntity(
    val idInt: Int,
    val taskName: String,
    val duration: Duration,
    val frequency: Frequency,
    val scheduledDate: LocalDate,
    val urgency: Urgency,
    val roomIdInt: Int,
    val roomName: String,
    val assigneeIdInt: Int,
    val assigneeName: String,
    val state: State,
) : Comparable<EnrichedTaskEntity>, Parcelable {
    override fun compareTo(other: EnrichedTaskEntity): Int =
        enrichedTaskComparator.compare(this, other)

    @IgnoredOnParcel
    @Ignore
    val id: Task.Id = Task.Id(idInt)

    @IgnoredOnParcel
    @Ignore
    val roomId: Room.Id = Room.Id(roomIdInt)

    @IgnoredOnParcel
    @Ignore
    val assigneeId: Resident.Id = Resident.Id(assigneeIdInt)
}