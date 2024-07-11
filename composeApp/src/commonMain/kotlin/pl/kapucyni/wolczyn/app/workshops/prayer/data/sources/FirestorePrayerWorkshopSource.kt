package pl.kapucyni.wolczyn.app.workshops.prayer.data.sources

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kapucyni.wolczyn.app.workshops.prayer.data.model.FirestorePrayerTask

class FirestorePrayerWorkshopSource {
    companion object {
        private const val WORKSHOPS_COLLECTION = "workshops"
        private const val PRAYER_DOCUMENT = "prayer"
        private const val TASKS_COLLECTION = "tasks"
    }

    fun getWorkshopTasks(): Flow<List<FirestorePrayerTask>> =
        Firebase.firestore.collection(WORKSHOPS_COLLECTION)
            .document(PRAYER_DOCUMENT)
            .collection(TASKS_COLLECTION)
            .snapshots
            .map { querySnapshot ->
                querySnapshot.documents.map { it.data() }
            }
}