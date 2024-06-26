package pl.kapucyni.wolczyn.app.weather.data.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.weather.data.sources.FirestoreWeatherSource
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather
import pl.kapucyni.wolczyn.app.weather.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val firestoreSource: FirestoreWeatherSource,
): WeatherRepository {
    override fun getWeather(): Flow<Weather> = firestoreSource.getWeather()
}