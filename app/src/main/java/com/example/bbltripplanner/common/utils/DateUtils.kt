package com.example.bbltripplanner.common.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock.System.now
import kotlin.time.Instant.Companion.fromEpochMilliseconds

object DateUtils {
    fun Long?.toFormattedDateString(): String {
        val instant = fromEpochMilliseconds(this ?: System.currentTimeMillis())
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

        val formatter = LocalDate.Format {
            day()
            char('/'); monthNumber(); char('/'); year()
        }
        return formatter.format(date)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    class FutureOrPresentSelectableDates(
        val endDate: Long?
    ): SelectableDates {
        private val timeZone = TimeZone.currentSystemDefault()
        private val today = now().toLocalDateTime(timeZone).date

        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val date = fromEpochMilliseconds(utcTimeMillis)
                .toLocalDateTime(timeZone).date
            return if (endDate != null) {
                date >= today && date <= fromEpochMilliseconds(
                    endDate
                ).toLocalDateTime(timeZone).date
            } else {
                date >= today
            }
        }

        override fun isSelectableYear(year: Int): Boolean {
            return year >= today.year
        }
    }

    class DayAfterStartDateSelectableDates(
        val startDate: Long?
    ): SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            if (startDate == null) {
                return false
            }
            val date = fromEpochMilliseconds(utcTimeMillis)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
            return date >= fromEpochMilliseconds(startDate)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
    }

    fun getTimeAgo(timeMillis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timeMillis

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            seconds < 60 -> "${seconds}s ago"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            days < 7 -> "${days}d ago"
            weeks < 5 -> "${weeks}w ago"
            months < 12 -> "${months}mo ago"
            else -> "${years}y ago"
        }
    }
}