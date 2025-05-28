package pl.kapucyni.wolczyn.app.meetings.presentation.participants

import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

sealed interface ParticipantsScreenAction {
    data class UpdateSearchQuery(val query: String) : ParticipantsScreenAction
    data class UpdateTypesFilter(val elementSelected: ParticipantType) : ParticipantsScreenAction
    data class UpdateWorkshopsFilter(val elementSelected: String) : ParticipantsScreenAction
}