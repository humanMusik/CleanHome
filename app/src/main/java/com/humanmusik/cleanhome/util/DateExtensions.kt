package com.humanmusik.cleanhome.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDate.toDateString(formatStyle: FormatStyle): String =
    format(DateTimeFormatter.ofLocalizedDate(formatStyle))

fun LocalDate.toDateString(pattern: String): String =
    format(DateTimeFormatter.ofPattern(pattern))
