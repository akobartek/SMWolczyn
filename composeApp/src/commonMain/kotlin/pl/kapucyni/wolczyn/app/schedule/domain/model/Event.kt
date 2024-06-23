package pl.kapucyni.wolczyn.app.schedule.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Event(
    val id: String = "",
    val time: LocalTime = LocalTime(0, 0),
    val name: String = "",
    val place: EventPlace = EventPlace.UNKNOWN,
    val type: EventType = EventType.OTHER,
    val guest: String? = null,
    val guestUrl: String? = null
) {
    fun  isEventOverOrLasting(): Boolean {
        val now = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .time
        return time < now
    }
}