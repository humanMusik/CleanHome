package com.humanmusik.cleanhome.data

import androidx.room.TypeConverter
import java.time.OffsetDateTime

object OffsetDateTimeTypeConverter {
    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let(OffsetDateTime::parse)
    }

    @TypeConverter
    fun fromOffsetDateTime(offsetDateTime: OffsetDateTime?): String? {
        return offsetDateTime?.toString()
    }
}