package com.example.bbltripplanner.common.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

object DateUtils {
    fun Long?.toFormattedDateString(): String {
        val instant = Instant.fromEpochMilliseconds(this ?: System.currentTimeMillis())
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

        val formatter = LocalDate.Format {
            dayOfMonth(); char('/'); monthNumber(); char('/'); year()
        }
        return formatter.format(date)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    object FutureOrPresentSelectableDates: SelectableDates {
        private val timeZone = TimeZone.currentSystemDefault()
        private val today = Clock.System.now().toLocalDateTime(timeZone).date

        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val date = Instant.fromEpochMilliseconds(utcTimeMillis)
                .toLocalDateTime(timeZone).date
            return date >= today
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
            val date = Instant.fromEpochMilliseconds(utcTimeMillis)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
            return date >= Instant.fromEpochMilliseconds(startDate)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
    }
}