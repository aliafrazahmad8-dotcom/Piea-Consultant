package com.piea.student.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val displayFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

    fun formatTimestamp(millis: Long): String {
        if (millis <= 0L) return "-"
        return displayFormat.format(Date(millis))
    }
}
