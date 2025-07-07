package com.humanmusik.cleanhome.data

import androidx.room.TypeConverter
import java.time.LocalDate

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