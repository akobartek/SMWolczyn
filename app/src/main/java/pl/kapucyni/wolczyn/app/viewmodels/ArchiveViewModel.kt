package pl.kapucyni.wolczyn.app.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import pl.kapucyni.wolczyn.app.model.ArchiveMeeting
import kotlin.coroutines.CoroutineContext

class ArchiveViewModel(val app: Application) : AndroidViewModel(app) {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val meetings = MutableLiveData<List<ArchiveMeeting>>()

    fun fetchMeetings() {
        scope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance().collection("archive").get().await()
                meetings.postValue(snapshot.toObjects())
            } catch (exc: FirebaseFirestoreException) {
                Log.e("Error catched", exc.toString())
            }
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}