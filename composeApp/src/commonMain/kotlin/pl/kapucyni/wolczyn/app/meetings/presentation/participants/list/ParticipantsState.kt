package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import androidx.compose.runtime.Immutable
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant

@Immutable
data class ParticipantsState(
    val meetingId: Int,
    val participants: List<Participant> = listOf(),
    val user: User,
    val listVisible: Boolean,
)
