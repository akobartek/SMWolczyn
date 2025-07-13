package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

sealed interface ParticipantsScreenAction {
    data class UpdateSearchQuery(val query: String) : ParticipantsScreenAction
    data class UpdateSorting(val sorting: ParticipantsSorting) : ParticipantsScreenAction
    data class ToggleAllUsers(val checked: Boolean) : ParticipantsScreenAction
    data class UpdateTypesFilter(val elementSelected: ParticipantType) : ParticipantsScreenAction
    data class UpdateWorkshopsFilter(val elementSelected: String) : ParticipantsScreenAction
    data class QrScanSuccess(val email: String) : ParticipantsScreenAction
    data object QrScanFailure : ParticipantsScreenAction
}