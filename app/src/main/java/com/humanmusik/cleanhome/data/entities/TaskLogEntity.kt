package com.humanmusik.cleanhome.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.humanmusik.cleanhome.data.LocalDateTypeConverter
import com.humanmusik.cleanhome.domain.model.ActionType
import java.time.LocalDate

@TypeConverters(
    LocalDateTypeConverter::class,
)
@Entity
data class TaskLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val taskId: String,
    val date: LocalDate,
    val recordedAction: ActionType,
)
