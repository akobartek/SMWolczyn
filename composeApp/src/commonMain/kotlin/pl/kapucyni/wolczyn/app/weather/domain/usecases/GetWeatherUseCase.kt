package pl.kapucyni.wolczyn.app.weather.domain.usecases

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather
import pl.kapucyni.wolczyn.app.weather.domain.repository.WeatherRepository

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {
    operator fun invoke(): Flow<Weather> = weatherRepository.getWeather()
}