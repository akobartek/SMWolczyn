package pl.kapucyni.wolczyn.app.weather.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather

interface WeatherRepository {
    fun getWeather(): Flow<Weather>
}