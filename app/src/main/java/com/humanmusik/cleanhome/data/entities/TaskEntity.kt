package com.humanmusik.cleanhome.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.humanmusik.cleanhome.data.OffsetDateTimeTypeConverter
import com.humanmusik.cleanhome.domain.model.Frequency
import com.humanmusik.cleanhome.domain.model.Urgency
import java.time.OffsetDateTime

@TypeConverters(OffsetDateTimeTypeConverter::class)
@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @Embedded(prefix = "room_")
    val room: RoomEntity,
    val duration: Long,
    val frequency: Frequency,
    val scheduledDate: OffsetDateTime,
    val urgency: Urgency,
    @Embedded(prefix = "resident_")
    val resident: ResidentEntity,
)

