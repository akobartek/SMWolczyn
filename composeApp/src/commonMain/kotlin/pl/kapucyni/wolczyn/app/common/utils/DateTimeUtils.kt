package pl.kapucyni.wolczyn.app.common.utils

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil
import kotlin.time.Clock
import kotlin.time.Instant

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
                day()
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
                day()
                char('.')
                monthNumber()
                char('.')
                year()
            }
        )
    }
)

fun Timestamp.getFormattedDate() =
    Instant.fromEpochSeconds(this.seconds, this.nanoseconds)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .getFormattedDate()

fun Long.getFormattedDate() =
    Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .getFormattedDate()

fun Long.isAgeBelow(age: Int, other: Instant = Clock.System.now()) =
    Instant.fromEpochMilliseconds(this)
        .yearsUntil(
            other = other,
            timeZone = TimeZone.currentSystemDefault(),
        ) < age

fun Long.getAge(other: Instant = Clock.System.now()) =
    Instant.fromEpochMilliseconds(this)
        .yearsUntil(
            other = other,
            timeZone = TimeZone.currentSystemDefault(),
        )

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
            in (1800..1899) -> chars((month.number + 80).toString())
            in (2000..2099) -> chars((month.number + 20).toString())
            in (2100..2199) -> chars((month.number + 40).toString())
            in (2200..2299) -> chars((month.number + 60).toString())
            else -> monthNumber()
        }
        day()
    }
)