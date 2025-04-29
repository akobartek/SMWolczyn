package pl.kapucyni.wolczyn.app.meetings.domain

import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop

interface MeetingsRepository {
    suspend fun getAvailableWorkshops(): List<Workshop>
    suspend fun checkPreviousSigning(meetingId: Int, email: String): Participant?
}