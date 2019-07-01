package pl.kapucyni.wolczyn.app.apicalls.weather

import pl.kapucyni.wolczyn.app.apicalls.BaseRepository
import pl.kapucyni.wolczyn.app.model.Weather

class WeatherRepository(private val api: WeatherApi) : BaseRepository() {

    suspend fun getWeatherFromApi(): Weather? = api.getWeatherForecastFromApiAsync()
}