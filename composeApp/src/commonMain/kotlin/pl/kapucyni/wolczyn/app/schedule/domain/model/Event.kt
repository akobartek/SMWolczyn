package pl.kapucyni.wolczyn.app.schedule.domain.model

import kotlinx.datetime.LocalTime

data class Event(
    val id: String = "",
    val hour: LocalTime = LocalTime(0, 0),
    val name: String = "",
    val place: EventPlace = EventPlace.UNKNOWN,
    val type: EventType = EventType.OTHER,
    val guest: String? = null,
    var videoUrl: String? = null
)
