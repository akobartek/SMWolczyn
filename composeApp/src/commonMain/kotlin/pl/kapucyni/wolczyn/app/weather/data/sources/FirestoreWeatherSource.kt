package pl.kapucyni.wolczyn.app.weather.data.sources

import dev.gitlive.firebase.firestore.FirebaseFirestore
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreDocument
import pl.kapucyni.wolczyn.app.common.utils.saveObject
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather

class FirestoreWeatherSource(
    private val firestore: FirebaseFirestore,
) {
    companion object {
        private const val WEATHER_COLLECTION = "weather_new"
        private const val WEATHER_DOCUMENT = "current"
    }

    fun getWeather() =
        firestore.getFirestoreDocument<Weather?>(
            collectionName = WEATHER_COLLECTION,
            documentId = WEATHER_DOCUMENT
        )

    suspend fun saveWeather(weather: Weather) =
        firestore.saveObject(
            collectionName = WEATHER_COLLECTION,
            id = WEATHER_DOCUMENT,
            data = weather,
        )
}