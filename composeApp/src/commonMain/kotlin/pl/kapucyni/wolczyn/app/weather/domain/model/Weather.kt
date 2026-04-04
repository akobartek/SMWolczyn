package pl.kapucyni.wolczyn.app.weather.domain.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val dayTemp: Int = 0,
    val nightTemp: Int = 0,
    val precipitationChance: Int = 0,
    val weatherCode: Int = 0,
    val lastUpdate: Timestamp? = null,
)
