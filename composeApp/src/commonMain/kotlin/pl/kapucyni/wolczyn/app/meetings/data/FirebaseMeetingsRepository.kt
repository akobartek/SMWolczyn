package pl.kapucyni.wolczyn.app.meetings.data

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType.ADMIN
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType.ANIMATORS_MANAGER
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType.SCOUTS_MANAGER
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollection
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollectionFlow
import pl.kapucyni.wolczyn.app.common.utils.saveObject
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.Meeting
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType.ANIMATOR
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType.SCOUT
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop

class FirebaseMeetingsRepository(
    private val firestore: FirebaseFirestore,
) : MeetingsRepository {

    override suspend fun getMeeting(id: Int): Meeting = runCatching {
        meetings()
            .document(id.toString())
            .get()
            .data<Meeting>()
    }.getOrDefault(Meeting(id = id))

    override suspend fun getAllWorkshops() = runCatching {
        firestore.getFirestoreCollection<Workshop>(COLLECTION_WORKSHOPS)
    }.getOrDefault(listOf())

    override suspend fun getAvailableWorkshops() =
        getAllWorkshops().filter { it.available }

    override suspend fun saveWorkshop(workshop: Workshop) = runCatching {
        firestore.saveObject(
            collectionName = COLLECTION_WORKSHOPS,
            id = workshop.id,
            data = workshop,
        )
    }.getOrDefault(Unit)

    override suspend fun checkPreviousSigning(
        meetingId: Int,
        email: String,
    ): Participant? = runCatching {
        signings(meetingId)
            .document(email)
            .get()
            .let {
                if (it.exists) it.data<Participant>()
                else null
            }
    }.getOrDefault(null)

    override suspend fun saveParticipant(meetingId: Int, participant: Participant) = runCatching {
        signings(meetingId)
            .document(participant.email)
            .set(participant)
    }

    override suspend fun removeParticipant(meetingId: Int, email: String) = runCatching {
        signings(meetingId)
            .document(email)
            .delete()
    }

    override suspend fun getParticipantsForGroups(meetingId: Int) = runCatching {
        signings(meetingId)
            .get()
            .documents
            .map { it.data<Participant>() }
            .filter { it.paid }
            .sortedBy { it.birthday.seconds }
    }.getOrDefault(listOf())

    override suspend fun getGroups(meetingId: Int) = runCatching {
        firestore
            .collection(COLLECTION_MEETINGS)
            .document(meetingId.toString())
            .collection(COLLECTION_GROUPS)
            .get()
            .documents
            .map { it.data<Group>() }
            .sortedBy { it.number }
    }.getOrDefault(listOf())

    override suspend fun saveGroups(meetingId: Int, groups: List<Group>) = runCatching {
        firestore.batch().let { batch ->
            groups.forEach { group ->
                batch.set(
                    documentRef = groups(meetingId).document(group.number.toString()),
                    data = group,
                )
            }
            batch.commit()
        }
    }

    override fun getAllMeetings(): Flow<List<Meeting>> =
        firestore.getFirestoreCollectionFlow<Meeting>(COLLECTION_MEETINGS)
            .map { meetings -> meetings.sortedByDescending { it.id } }

    override fun getMeetingParticipants(
        meetingId: Int,
        userType: UserType,
    ): Flow<List<Participant>> = runCatching {
        signings(meetingId)
            .snapshots
            .map<QuerySnapshot, List<Participant>> { querySnapshot ->
                querySnapshot.documents.map { it.data() }
            }
            .map { list ->
                when (userType) {
                    ADMIN -> list
                    SCOUTS_MANAGER -> list.filter { it.type == SCOUT }
                    ANIMATORS_MANAGER -> list.filter { it.type == ANIMATOR }
                    else -> emptyList()
                }
            }
            .map { list ->
                val locale = Locale.current
                list.sortedWith(
                    compareBy(
                        {
                            it.firstName.toLowerCase(locale)
                                .replace("br. ", "")
                                .replace("s. ", "")
                        },
                        { it.lastName.toLowerCase(locale) },
                    )
                )
            }
    }.getOrDefault(emptyFlow())

    override fun getWorkshopsFlow(): Flow<List<Workshop>> = runCatching {
        firestore.getFirestoreCollectionFlow<Workshop>(COLLECTION_WORKSHOPS)
    }.getOrDefault(emptyFlow())

    private fun meetings() =
        firestore.collection(COLLECTION_MEETINGS)

    private fun signings(meetingId: Int) =
        meetings()
            .document(meetingId.toString())
            .collection(COLLECTION_SIGNINGS)

    private fun groups(meetingId: Int) =
        meetings()
            .document(meetingId.toString())
            .collection(COLLECTION_GROUPS)

    private companion object {
        const val COLLECTION_MEETINGS = "meetings"
        const val COLLECTION_SIGNINGS = "signings"
        const val COLLECTION_GROUPS = "groups"
        const val COLLECTION_WORKSHOPS = "workshops"
    }
}