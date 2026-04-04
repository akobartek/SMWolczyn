package pl.kapucyni.wolczyn.app.weather.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val daily: DailyData,
)