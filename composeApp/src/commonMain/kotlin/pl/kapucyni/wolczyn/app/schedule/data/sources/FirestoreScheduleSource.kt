package pl.kapucyni.wolczyn.app.schedule.data.sources

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollectionFlow
import pl.kapucyni.wolczyn.app.schedule.data.model.FirestoreEvent

class FirestoreScheduleSource {

    companion object {
        private const val SCHEDULE_COLLECTION = "schedule"
    }

    fun getSchedule(): Flow<List<FirestoreEvent>> =
        Firebase.firestore.getFirestoreCollectionFlow(SCHEDULE_COLLECTION)
}