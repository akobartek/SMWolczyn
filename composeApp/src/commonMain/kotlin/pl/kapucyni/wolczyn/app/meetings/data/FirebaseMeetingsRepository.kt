package pl.kapucyni.wolczyn.app.meetings.data

import dev.gitlive.firebase.firestore.FirebaseFirestore
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollection
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop

class FirebaseMeetingsRepository(
    private val firestore: FirebaseFirestore,
) : MeetingsRepository {

    override suspend fun getAvailableWorkshops() = runCatching {
        firestore.getFirestoreCollection<Workshop>(WORKSHOPS_COLLECTION)
    }.getOrDefault(listOf())
        .filter { it.available }

    override suspend fun checkPreviousSigning(
        meetingId: Int,
        email: String,
    ): Participant? = kotlin.runCatching {
        firestore
            .collection(MEETINGS_COLLECTION)
            .document(meetingId.toString())
            .collection(SIGNINGS_COLLECTION)
            .document(email)
            .get()
            .let {
                if (it.exists) it.data<Participant>()
                else null
            }
    }.getOrElse {
        println(it)
        null
    }

    private companion object {
        const val MEETINGS_COLLECTION = "meetings"
        const val SIGNINGS_COLLECTION = "signings"
        const val GROUPS_COLLECTION = "groups"
        const val WORKSHOPS_COLLECTION = "workshops"
    }
}