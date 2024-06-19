package pl.kapucyni.wolczyn.app.schedule.data.sources

import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kapucyni.wolczyn.app.schedule.data.model.FirestoreEvent

private const val SCHEDULE_COLLECTION = "schedule"

internal fun FirebaseFirestore.getFirestoreSchedule(): Flow<List<FirestoreEvent>> =
    this.collection(SCHEDULE_COLLECTION)
        .snapshots
        .map { querySnapshot ->
            querySnapshot.documents.map { it.data() }
        }