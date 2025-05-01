package pl.kapucyni.wolczyn.app.common.utils

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.getFormattedDateTime() = format(
    LocalDateTime.Format {
        time(
            LocalTime.Format {
                hour()
                char(':')
                minute()
                char(':')
                second()
            }
        )
        chars(" - ")
        date(
            LocalDate.Format {
                dayOfMonth()
                char('.')
                monthNumber()
                char('.')
                year()
            }
        )
    }
)

fun LocalDateTime.getFormattedDate() = format(
    LocalDateTime.Format {
        date(
            LocalDate.Format {
                dayOfMonth()
                char('.')
                monthNumber()
                char('.')
                year()
            }
        )
    }
)

fun Long.getFormattedDate() =
    Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .getFormattedDate()

fun Timestamp.getPeselBeginning() =
    Instant.fromEpochSeconds(this.seconds, this.nanoseconds)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .getPeselBeginning()

fun Long.getPeselBeginning() =
    Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .getPeselBeginning()

private fun LocalDateTime.getPeselBeginning() = format(
    LocalDateTime.Format {
        yearTwoDigits(year)
        when (year) {
            in (1800..1899) -> chars((monthNumber + 80).toString())
            in (2000..2099) -> chars((monthNumber + 20).toString())
            in (2100..2199) -> chars((monthNumber + 40).toString())
            in (2200..2299) -> chars((monthNumber + 60).toString())
            else -> monthNumber()
        }
        dayOfMonth()
    }
)