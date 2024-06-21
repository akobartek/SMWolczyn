package pl.kapucyni.wolczyn.app.weather.data.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.weather.data.sources.getFirestoreWeather
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather
import pl.kapucyni.wolczyn.app.weather.domain.repository.WeatherRepository

class WeatherRepositoryImpl: WeatherRepository {
    override fun getWeather(): Flow<Weather> = getFirestoreWeather()
}