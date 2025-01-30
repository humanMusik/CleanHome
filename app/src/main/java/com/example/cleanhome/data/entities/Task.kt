package com.example.cleanhome.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cleanhome.data.OffsetDateTimeTypeConverter
import java.time.OffsetDateTime

@TypeConverters(OffsetDateTimeTypeConverter::class)
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = null,
    val taskName: String,
    val roomId: Int,
    val duration: Long,
    val frequency: Frequency,
    val scheduledDate: OffsetDateTime,
    val urgency: Urgency,
    val assignedTo: Int,
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