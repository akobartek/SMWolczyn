package pl.kapucyni.wolczyn.app.weather.data.sources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pl.kapucyni.wolczyn.app.weather.data.model.WeatherResponse

class OpenMeteoWeatherSource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun fetchWeather(
        lat: Double = 51.01845,
        lon: Double = 18.04994,
    ): WeatherResponse? = runCatching {
        client.get("https://api.open-meteo.com/v1/forecast") {
            parameter("latitude", lat)
            parameter("longitude", lon)
            parameter("daily", "temperature_2m_max,temperature_2m_min,precipitation_probability_max,weather_code")
            parameter("timezone", "auto")
        }.body<WeatherResponse>()
    }.getOrNull()
}