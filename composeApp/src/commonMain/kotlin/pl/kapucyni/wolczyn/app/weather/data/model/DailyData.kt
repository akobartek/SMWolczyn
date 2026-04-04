package pl.kapucyni.wolczyn.app.weather.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyData(
    val time: List<String>,
    @SerialName("temperature_2m_max")
    val tempMax: List<Double>,
    @SerialName("temperature_2m_min")
    val tempMin: List<Double>,
    @SerialName("precipitation_probability_max")
    val precipProb: List<Int>,
    @SerialName("weather_code")
    val weatherCodes: List<Int>,
)