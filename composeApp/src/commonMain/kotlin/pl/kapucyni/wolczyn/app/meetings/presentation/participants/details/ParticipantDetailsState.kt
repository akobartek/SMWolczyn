package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

import androidx.compose.runtime.Immutable
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant

@Immutable
data class ParticipantDetailsState(
    val participant: Participant,
    val showData: Boolean,
    val meetingsCount: Int?,
)
