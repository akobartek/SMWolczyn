package pl.kapucyni.wolczyn.app.weather.data.repository

import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import pl.kapucyni.wolczyn.app.weather.data.sources.FirestoreWeatherSource
import pl.kapucyni.wolczyn.app.weather.data.sources.OpenMeteoWeatherSource
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather
import pl.kapucyni.wolczyn.app.weather.domain.repository.WeatherRepository
import kotlin.time.Clock
import kotlin.time.Instant

class WeatherRepositoryImpl(
    private val firestoreSource: FirestoreWeatherSource,
    private val openMeteoSource: OpenMeteoWeatherSource,
) : WeatherRepository {

    private val requestMutex = Mutex()

    override fun getWeather(): Flow<Weather?> = firestoreSource.getWeather()
        .onEach { weather ->
            if (weather == null || weather.lastUpdate.needUpdate()) {
                CoroutineScope(Dispatchers.Default).launch {
                    fetchWeather()
                }
            }
        }

    private fun Timestamp?.needUpdate(): Boolean {
        if (this == null) return true
        val now = Clock.System.now()
        val diff = now - Instant.fromEpochMilliseconds(this.toMilliseconds().toLong())
        return diff.inWholeHours >= 12
    }

    private suspend fun fetchWeather() {
        if (!requestMutex.tryLock()) return
        try {
            val response = openMeteoSource.fetchWeather()
            response?.daily?.let { data ->
                val newWeather = Weather(
                    dayTemp = data.tempMax.first().toInt(),
                    nightTemp = data.tempMin.first().toInt(),
                    precipitationChance = data.precipProb.first(),
                    weatherCode = data.weatherCodes.first(),
                    lastUpdate = Timestamp.now(),
                )
                firestoreSource.saveWeather(newWeather)
            }
        } finally {
            requestMutex.unlock() // Zawsze zwalniamy, nawet po błędzie!
        }
    }
}