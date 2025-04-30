package pl.kapucyni.wolczyn.app.meetings.data

import dev.gitlive.firebase.firestore.FirebaseFirestore
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollection
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Meeting
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop

class FirebaseMeetingsRepository(
    private val firestore: FirebaseFirestore,
) : MeetingsRepository {

    override suspend fun getMeeting(id: Int): Meeting = runCatching {
        firestore
            .collection(COLLECTION_MEETINGS)
            .document(id.toString())
            .get()
            .data<Meeting>()
    }.getOrDefault(Meeting(id = id))

    override suspend fun getAvailableWorkshops() = runCatching {
        firestore.getFirestoreCollection<Workshop>(COLLECTION_WORKSHOPS)
    }.getOrDefault(listOf())
        .filter { it.available }

    override suspend fun checkPreviousSigning(
        meetingId: Int,
        email: String,
    ): Participant? = kotlin.runCatching {
        firestore
            .collection(COLLECTION_MEETINGS)
            .document(meetingId.toString())
            .collection(COLLECTION_SIGNINGS)
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

    override suspend fun saveParticipant(meetingId: Int, participant: Participant) = runCatching {
        firestore
            .collection(COLLECTION_MEETINGS)
            .document(meetingId.toString())
            .collection(COLLECTION_SIGNINGS)
            .document(participant.email)
            .set(participant)
    }

    private companion object {
        const val COLLECTION_MEETINGS = "meetings"
        const val COLLECTION_SIGNINGS = "signings"
        const val COLLECTION_GROUPS = "groups"
        const val COLLECTION_WORKSHOPS = "workshops"
    }
}