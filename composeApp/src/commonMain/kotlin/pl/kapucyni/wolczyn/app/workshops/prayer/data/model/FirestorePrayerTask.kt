package pl.kapucyni.wolczyn.app.workshops.prayer.data.model

import kotlinx.serialization.Serializable
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.model.PrayerWorkshopTask

@Serializable
data class FirestorePrayerTask(
    val name: String = "",
    val photoUrl: String = "",
    val isAvailable: Boolean = false,
    val order: Int = 0,
) {
    fun toDomainObject() =
        PrayerWorkshopTask(
            name = name,
            photoUrl = photoUrl,
        )
}