package com.humanmusik.cleanhome.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ForeignKey.Companion.SET_NULL
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.humanmusik.cleanhome.data.DurationTypeConverter
import com.humanmusik.cleanhome.data.LocalDateTypeConverter
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Urgency
import java.time.LocalDate
import kotlin.time.Duration

const val TASK_ENTITY_ROOM_ID = "roomId"
const val TASK_ENTITY_ASSIGNEE_ID = "assigneeId"

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
)

