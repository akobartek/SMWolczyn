package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant

data class ParticipantDetailsState(
    val participant: Participant,
    val showData: Boolean,
)
