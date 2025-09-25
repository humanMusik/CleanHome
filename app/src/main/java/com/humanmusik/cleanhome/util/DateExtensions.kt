package com.humanmusik.cleanhome.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.TimeZone

fun LocalDate.toDateString(formatStyle: FormatStyle): String =
    format(DateTimeFormatter.ofLocalizedDate(formatStyle))

fun LocalDate.toDateString(pattern: String): String =
    format(DateTimeFormatter.ofPattern(pattern))

fun Instant.toLocalDate(): LocalDate =
    atZone(ZoneId.systemDefault()).toLocalDate()

fun Instant.toDateString(formatStyle: FormatStyle): String =
    toLocalDate().toDateString(formatStyle)

fun LocalDate.toEpochMillis(): Long =
    LocalDateTime.of(this, LocalTime.MIDNIGHT).toInstant(ZoneOffset.UTC).toEpochMilli()
