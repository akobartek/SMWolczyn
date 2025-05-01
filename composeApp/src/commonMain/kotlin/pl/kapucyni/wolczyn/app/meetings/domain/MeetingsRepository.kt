package pl.kapucyni.wolczyn.app.meetings.domain

import pl.kapucyni.wolczyn.app.meetings.domain.model.Meeting
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop

interface MeetingsRepository {
    suspend fun getMeeting(id: Int): Meeting
    suspend fun getAvailableWorkshops(): List<Workshop>
    suspend fun checkPreviousSigning(meetingId: Int, email: String): Participant?
    suspend fun saveParticipant(meetingId: Int, participant: Participant): Result<Unit>
    suspend fun removeParticipant(meetingId: Int, email: String): Result<Unit>
}