package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant

data class ParticipantsState(
    val meetingId: Int,
    val participants: List<Participant>,
    val userType: UserType,
)
