package pl.kapucyni.wolczyn.app.weather.data.sources

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreDocument
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather

class FirestoreWeatherSource {
    companion object {
        private const val WEATHER_COLLECTION = "weather"
        private const val WEATHER_DOCUMENT = "current"
    }

    fun getWeather() =
        Firebase.firestore.getFirestoreDocument<Weather?>(
            collectionName = WEATHER_COLLECTION,
            documentId = WEATHER_DOCUMENT
        )
}