package com.humanmusik.cleanhome.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.humanmusik.cleanhome.data.DurationTypeConverter
import com.humanmusik.cleanhome.data.LocalDateTypeConverter
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Urgency
import java.time.LocalDate
import kotlin.time.Duration

@TypeConverters(
    LocalDateTypeConverter::class,
    DurationTypeConverter::class,
)
@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @Embedded(prefix = "room_")
    val room: RoomEntity,
    val duration: Duration,
    val frequency: Frequency,
    val scheduledDate: LocalDate,
    val urgency: Urgency,
    @Embedded(prefix = "resident_")
    val assignedTo: ResidentEntity,
)

