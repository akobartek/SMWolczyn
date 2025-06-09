package pl.kapucyni.wolczyn.app.meetings.presentation.workshops

import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop

sealed interface MeetingWorkshopsScreenAction {

    data class UpdateWorkshop(
        val workshop: Workshop,
        val available: Boolean,
    ) : MeetingWorkshopsScreenAction

    data class UpdateIsAdding(val isAdding: Boolean) : MeetingWorkshopsScreenAction

    data class SaveWorkshop(val workshopName: String) : MeetingWorkshopsScreenAction
}