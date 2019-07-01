package pl.kapucyni.wolczyn.app.apicalls.weather

import pl.kapucyni.wolczyn.app.model.Weather

class WeatherRepository(private val api: WeatherApi) {

    suspend fun getWeatherFromApi(): Weather? = api.getWeatherForecastFromApiAsync()
}