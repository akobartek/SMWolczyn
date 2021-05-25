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
import pl.kapucyni.wolczyn.app.model.EventFirestore

class ScheduleViewModel(val app: Application) : AndroidViewModel(app) {

    val eventsFromFirestore = MutableLiveData<List<EventFirestore>>()

    fun fetchSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val snapshot =
                    FirebaseFirestore.getInstance().collection("schedule").get().await()
                val events = snapshot.toObjects<EventFirestore>()
                eventsFromFirestore.postValue(events)
            } catch (exc: FirebaseFirestoreException) {
                Log.e("Error catched", exc.toString())
            }
        }
    }
}