package com.mp.momentrip.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatDateRange(start: LocalDate?, end: LocalDate?): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)
    val startStr = start?.format(formatter) ?: ""
    val endStr = end?.format(formatter) ?: ""

    return when {
        start != null && end != null -> "$startStr – $endStr"
        start != null -> startStr
        end != null -> endStr
        else -> ""
    }
}