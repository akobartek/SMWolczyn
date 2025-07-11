package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

data class ParticipantsFilterState(
    val query: String = "",
    val onlyConfirmedParticipants: Boolean = false,
    val participantTypes: List<ParticipantType> = ParticipantType.entries.toList(),
    val selectedTypes: List<ParticipantType> = emptyList(),
    val workshops: List<String> = emptyList(),
    val selectedWorkshops: List<String> = emptyList(),
)
