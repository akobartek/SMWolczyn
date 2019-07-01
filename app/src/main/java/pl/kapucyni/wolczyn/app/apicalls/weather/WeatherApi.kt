package pl.kapucyni.wolczyn.app.apicalls.weather

import pl.kapucyni.wolczyn.app.BuildConfig
import pl.kapucyni.wolczyn.app.model.Weather
import retrofit2.http.GET

interface WeatherApi {

    @GET("forecast?lat=50.97&lon=18.22&units=metric&appid=${BuildConfig.WEATHER_API_KEY}")
    suspend fun getWeatherForecastFromApiAsync(): Weather
}