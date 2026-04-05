package com.example.bbltripplanner.common.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    fun formatLongToDate(time: Long, pattern: String = "MMM dd"): String {
        val date = Date(time)
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date)
    }
}
