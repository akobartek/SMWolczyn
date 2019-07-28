package pl.kapucyni.wolczyn.app.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pl.kapucyni.wolczyn.app.apicalls.RetrofitClient
import pl.kapucyni.wolczyn.app.apicalls.weather.WeatherRepository
import pl.kapucyni.wolczyn.app.model.Weather
import pl.kapucyni.wolczyn.app.model.WeatherRecord

class WeatherViewModel(val app: Application) : AndroidViewModel(app) {

    private val repository: WeatherRepository = WeatherRepository(RetrofitClient.weatherApi)

    val weatherRecords = MutableLiveData<List<WeatherRecord>>()

    fun fetchWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherRef = FirebaseFirestore.getInstance().collection("weather")
            val now = System.currentTimeMillis()
            try {
                val snapshot = weatherRef.get().await()
                val weatherData = snapshot.toObjects<Weather>()
                if (weatherData.isNotEmpty() && snapshot.documents[0].id.toLong() > now - 5400000) {
                    Log.i("WeatherViewModel", "Data loaded from firebase!")
                    weatherRecords.postValue(weatherData[0].list)
                } else {
                    val weather = repository.getWeatherFromApi()
                    if (weather != null) {
                        snapshot.documents.forEach { document -> weatherRef.document(document.id).delete() }
                        weatherRef.document(now.toString()).set(Weather(weather.list))
                    }
                    weatherRecords.postValue(weather?.list)
                }
            } catch (exc: FirebaseFirestoreException) {
                Log.e("Error catched", exc.toString())
                val weather = repository.getWeatherFromApi()
                if (weather != null) {
                    weatherRef.document(now.toString()).set(Weather(weather.list))
                }
                weatherRecords.postValue(weather?.list)
            }
        }
    }
}