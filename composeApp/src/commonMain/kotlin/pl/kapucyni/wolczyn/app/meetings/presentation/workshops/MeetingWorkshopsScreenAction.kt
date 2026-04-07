package pl.kapucyni.wolczyn.app.meetings.presentation.workshops

import pl.kapucyni.wolczyn.app.meetings.domain.model.Gender
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop

sealed interface MeetingWorkshopsScreenAction {

    data class UpdateAvailability(
        val workshop: Workshop,
        val available: Boolean,
    ) : MeetingWorkshopsScreenAction

    data class UpdateGender(
        val workshop: Workshop,
        val gender: Gender,
    ) : MeetingWorkshopsScreenAction

    data class UpdateIsAdding(val isAdding: Boolean) : MeetingWorkshopsScreenAction

    data class SaveWorkshop(val workshopName: String) : MeetingWorkshopsScreenAction
}