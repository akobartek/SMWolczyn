package pl.kapucyni.wolczyn.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonEncodingException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.apicalls.RetrofitClient
import pl.kapucyni.wolczyn.app.apicalls.wolczyn.KapucyniApiRepository
import pl.kapucyni.wolczyn.app.model.Departure

class DeparturesViewModel(val app: Application) : AndroidViewModel(app) {

    private val repository: KapucyniApiRepository = KapucyniApiRepository(RetrofitClient.kapucyniApi)

    val departures = MutableLiveData<MutableList<Departure>>()

    fun fetchDepartures() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                departures.postValue(repository.getDepartures())
            } catch (exc: JsonEncodingException) {
                departures.postValue(mutableListOf())
            }
        }
    }
}