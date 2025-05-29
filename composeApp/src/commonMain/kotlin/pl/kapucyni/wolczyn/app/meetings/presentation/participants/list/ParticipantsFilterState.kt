package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

data class ParticipantsFilterState(
    val query: String = "",
    val participantTypes: List<ParticipantType> = ParticipantType.entries.toList(),
    val selectedTypes: List<ParticipantType> = ParticipantType.entries.toList(),
    val workshops: List<String> = emptyList(),
    val selectedWorkshops: List<String> = emptyList(),
)
