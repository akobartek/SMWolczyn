package pl.kapucyni.wolczyn.app.meetings.presentation.groups

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType.ANIMATOR
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.DrawGroups
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnAnimatorClicked
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.SaveGroups
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.ToggleAnimatorsDialog

class MeetingGroupsViewModel(
    private val meetingId: Int,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<MeetingGroupsScreenState>() {

    init {
        viewModelScope.launch(Dispatchers.Default) {
            coroutineScope {
                val participants = async { meetingsRepository.getParticipantsForGroups(meetingId) }
                val groups = async { meetingsRepository.getGroups(meetingId) }

                val (members, animators) = participants.await().let { list ->
                    val locale = Locale.current

                    list.filter { it.type.canBeGroupMember() }
                        .sortedWith(
                            compareBy(
                                { it.type },
                                { it.firstName.toLowerCase(locale) },
                                { it.lastName.toLowerCase(locale) },
                            )
                        ) to list.filter { it.type.canBeAnimator() }
                        .sortedWith(
                            compareBy(
                                { it.type },
                                {
                                    it.firstName.toLowerCase(locale)
                                        .replace("br. ", "")
                                        .replace("s. ", "")
                                },
                                { it.lastName.toLowerCase(locale) },
                            )
                        )
                }
                val savedGroups = groups.await()

                _screenState.update {
                    State.Success(
                        data = MeetingGroupsScreenState(
                            newGroups = savedGroups,
                            savedGroups = savedGroups,
                            participants = members,
                            potentialAnimators = animators,
                            selectedAnimators = animators.filter { it.type == ANIMATOR },
                        )
                    )
                }
            }
        }
    }

    fun handleAction(action: MeetingGroupsScreenAction) {
        when (action) {
            is ToggleAnimatorsDialog -> toggleAnimatorsDialog()
            is OnAnimatorClicked -> onAnimatorClicked(action.participant)
            is DrawGroups -> drawGroups()
            is SaveGroups -> saveGroups()
        }
    }

    private fun toggleAnimatorsDialog() {
        _screenState.update {
            (it as? State.Success)?.data?.let { state ->
                State.Success(
                    data = state.copy(
                        animatorsDialogVisible = state.animatorsDialogVisible.not(),
                    )
                )
            } ?: it
        }
    }

    private fun onAnimatorClicked(animator: Participant) {
        if (animator.type == ANIMATOR) return

        _screenState.update {
            (it as? State.Success)?.data?.let { state ->
                val animators = state.selectedAnimators
                State.Success(
                    data = state.copy(
                        selectedAnimators =
                            if (animators.contains(animator)) animators - animator
                            else animators + animator,
                    )
                )
            } ?: it
        }
    }

    private fun drawGroups() {
        val state = (_screenState.value as? State.Success<MeetingGroupsScreenState>)?.data ?: return
        println(state)
    }

    private fun saveGroups() {
        val state = (_screenState.value as? State.Success<MeetingGroupsScreenState>)?.data ?: return
        if (state.saveAvailable.not() || state.newGroups.isEmpty()) return

        viewModelScope.launch(Dispatchers.Default) {
            meetingsRepository.saveGroups(meetingId, state.newGroups)
        }
    }
}