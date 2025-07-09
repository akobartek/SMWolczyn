package pl.kapucyni.wolczyn.app.meetings.presentation.groups

import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant

sealed interface MeetingGroupsScreenAction {

    data object ToggleAnimatorsDialog : MeetingGroupsScreenAction

    data object DrawGroups : MeetingGroupsScreenAction

    data object SaveGroups : MeetingGroupsScreenAction

    data class OnAnimatorClicked(
        val participant: Participant,
        val selected: Boolean,
    ) : MeetingGroupsScreenAction

    data class OnAnimatorDataChange(
        val currentGroupNumber: Int,
        val groupNumber: Int,
        val contactNumber: String,
    ) : MeetingGroupsScreenAction
}