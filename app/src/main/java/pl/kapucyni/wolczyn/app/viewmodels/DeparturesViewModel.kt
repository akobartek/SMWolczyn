package pl.kapucyni.wolczyn.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import pl.kapucyni.wolczyn.app.apicalls.RetrofitClient
import pl.kapucyni.wolczyn.app.apicalls.wolczyn.KapucyniApiRepository
import pl.kapucyni.wolczyn.app.model.Departure
import kotlin.coroutines.CoroutineContext

class DeparturesViewModel(val app: Application) : AndroidViewModel(app) {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: KapucyniApiRepository = KapucyniApiRepository(RetrofitClient.kapucyniApi)

    val departures = MutableLiveData<MutableList<Departure>>()

    fun fetchDepartures() {
        scope.launch {
            departures.postValue(repository.getDepartures())
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}