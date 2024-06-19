package pl.kapucyni.wolczyn.app.schedule.data.model

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import pl.kapucyni.wolczyn.app.schedule.domain.model.Event
import pl.kapucyni.wolczyn.app.schedule.domain.model.EventPlace
import pl.kapucyni.wolczyn.app.schedule.domain.model.EventType

@Serializable
data class FirestoreEvent(
    val id: String = "",
    val time: String = "",
    val name: String = "",
    val place: EventPlace = EventPlace.UNKNOWN,
    val type: EventType = EventType.OTHER,
    val guest: String? = null,
    var videoUrl: String? = null
) {
    fun copyToDomainObject(event: Event) = event.copy(
        time =
        if (time.isBlank()) event.time
        else time.split(":").let {
            LocalTime(it[0].toInt(), it.getOrNull(1)?.toInt() ?: 0)
        },
        name = name.ifBlank { event.name },
        place = if (place == EventPlace.UNKNOWN) event.place else place,
        type = if (type == EventType.OTHER) event.type else type,
        guest = if (guest.isNullOrBlank()) event.guest else guest,
        videoUrl = if (videoUrl.isNullOrBlank()) event.videoUrl else videoUrl
    )
}