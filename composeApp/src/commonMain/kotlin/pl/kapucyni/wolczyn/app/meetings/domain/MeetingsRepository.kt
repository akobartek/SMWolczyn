package pl.kapucyni.wolczyn.app.meetings.domain

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.Meeting
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop

interface MeetingsRepository {
    suspend fun getMeeting(id: Int): Meeting
    suspend fun getAllWorkshops(): List<Workshop>
    suspend fun getAvailableWorkshops(): List<Workshop>
    suspend fun saveWorkshop(workshop: Workshop)
    suspend fun getParticipant(meetingId: Int, email: String): Participant?
    suspend fun saveParticipant(meetingId: Int, participant: Participant): Result<Unit>
    suspend fun removeParticipant(meetingId: Int, email: String): Result<Unit>
    suspend fun getParticipantsForGroups(meetingId: Int): List<Participant>
    suspend fun getGroups(meetingId: Int): List<Group>
    suspend fun saveGroups(meetingId: Int, groups: List<Group>): Result<Unit>
    suspend fun getParticipantGroup(meetingId: Int, email: String): Group?

    fun getAllMeetings(): Flow<List<Meeting>>
    fun getMeetingParticipants(meetingId: Int, userType: UserType): Flow<List<Participant>>
    fun getWorkshopsFlow(): Flow<List<Workshop>>
    fun getParticipantFlow(meetingId: Int, email: String): Flow<Participant?>
}