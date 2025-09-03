package com.humanmusik.cleanhome.data

import androidx.room.TypeConverter
import java.time.LocalDate
import kotlin.time.Duration

object LocalDateTypeConverter {
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let(LocalDate::parse)
    }

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): String? {
        return localDate?.toString()
    }
}

object DurationTypeConverter {
    @TypeConverter
    fun toDuration(value: String?): Duration? {
        return value?.let(Duration::parse)
    }

    @TypeConverter
    fun fromDuration(duration: Duration?): String? {
        return duration?.toString()
    }
}