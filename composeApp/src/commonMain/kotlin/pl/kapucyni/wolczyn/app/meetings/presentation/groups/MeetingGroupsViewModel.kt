package pl.kapucyni.wolczyn.app.meetings.presentation.groups

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType.ANIMATOR
import pl.kapucyni.wolczyn.app.meetings.domain.model.toGroupMembers
import pl.kapucyni.wolczyn.app.meetings.domain.usecases.DrawGroupsUseCase
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.DrawGroups
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnAnimatorClicked
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnAnimatorDataChange
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnMemberGroupAdd
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnMemberGroupChange
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.SaveGroups
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.ToggleAnimatorsDialog
import kotlin.collections.contains

class MeetingGroupsViewModel(
    private val meetingId: Int,
    private val meetingsRepository: MeetingsRepository,
    private val drawGroupsUseCase: DrawGroupsUseCase,
) : BasicViewModel<MeetingGroupsScreenState>() {

    init {
        updateState()
    }

    fun handleAction(action: MeetingGroupsScreenAction) {
        when (action) {
            is ToggleAnimatorsDialog -> toggleAnimatorsDialog()
            is DrawGroups -> drawGroups()
            is SaveGroups -> saveGroups()

            is OnAnimatorClicked -> onAnimatorClicked(action.participant)
            is OnAnimatorDataChange -> onAnimatorDataChange(
                currentGroup = action.currentGroupNumber,
                newGroup = action.groupNumber,
                contact = action.contactNumber,
            )

            is OnMemberGroupAdd -> onMemberGroupAdd(
                newGroup = action.groupNumber,
                email = action.email,
            )

            is OnMemberGroupChange -> onMemberGroupChange(
                currentGroup = action.currentGroupNumber,
                newGroup = action.groupNumber,
                email = action.email,
            )
        }
    }

    private fun updateState() {
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
                            membersWithoutGroup = members.membersWithoutGroup(savedGroups),
                            potentialAnimators = animators,
                            selectedAnimators = animators.filter { it.type == ANIMATOR },
                            copyAvailable = savedGroups.isNotEmpty(),
                            loading = false,
                        )
                    )
                }
            }
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

    private fun onAnimatorDataChange(currentGroup: Int, newGroup: Int, contact: String) {
        val state = (_screenState.value as? State.Success)?.data ?: return

        state.newGroups.firstOrNull { it.number == currentGroup }?.let { group ->
            if (currentGroup == newGroup && group.animatorContact == contact) return

            val updatedGroup = group.copy(animatorContact = contact)
            val newGroups = state.newGroups.toMutableList()

            if (currentGroup != newGroup) {
                state.newGroups.firstOrNull { it.number == newGroup }?.let { targetGroup ->
                    newGroups[newGroups.indexOf(group)] = group.copy(
                        animatorName = targetGroup.animatorName,
                        animatorMail = targetGroup.animatorMail,
                        animatorContact = targetGroup.animatorContact,
                    )
                    newGroups[newGroups.indexOf(targetGroup)] = targetGroup.copy(
                        animatorName = updatedGroup.animatorName,
                        animatorMail = updatedGroup.animatorMail,
                        animatorContact = updatedGroup.animatorContact,
                    )
                }
            } else {
                newGroups[newGroups.indexOf(group)] = updatedGroup
            }

            _screenState.update {
                State.Success(
                    state.copy(
                        newGroups = newGroups,
                        saveAvailable = true,
                    )
                )
            }
        }
    }

    private fun onMemberGroupAdd(newGroup: Int, email: String) {
        val state = (_screenState.value as? State.Success)?.data ?: return
        val new = state.newGroups.firstOrNull { it.number == newGroup }
        val withoutGroup = state.membersWithoutGroup
        withoutGroup.remove(email)?.let { data -> new?.members?.put(email, data) }

        val newGroups = state.newGroups.toMutableList()
        newGroups[newGroups.indexOfFirst { it.number == newGroup }] = new ?: return

        _screenState.update {
            State.Success(
                state.copy(
                    membersWithoutGroup = withoutGroup,
                    newGroups = newGroups,
                    saveAvailable = true,
                )
            )
        }
    }

    private fun onMemberGroupChange(currentGroup: Int, newGroup: Int, email: String) {
        val state = (_screenState.value as? State.Success)?.data ?: return
        val current = state.newGroups.firstOrNull { it.number == currentGroup }
        val new = state.newGroups.firstOrNull { it.number == newGroup }
        current?.members?.remove(email)?.let { data -> new?.members?.put(email, data) }

        val newGroups = state.newGroups.toMutableList()
        newGroups[newGroups.indexOfFirst { it.number == currentGroup }] = current ?: return
        newGroups[newGroups.indexOfFirst { it.number == newGroup }] = new ?: return

        _screenState.update {
            State.Success(
                state.copy(
                    newGroups = newGroups,
                    saveAvailable = true,
                )
            )
        }
    }

    private fun drawGroups() {
        val state = (_screenState.value as? State.Success)?.data ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val groups = drawGroupsUseCase(
                participants = state.participants,
                animators = state.selectedAnimators,
            )
            _screenState.update {
                State.Success(
                    state.copy(
                        newGroups = groups,
                        membersWithoutGroup = state.participants.membersWithoutGroup(groups),
                        saveAvailable = true,
                    )
                )
            }
        }
    }

    private fun saveGroups() {
        val state = (_screenState.value as? State.Success)?.data ?: return
        if (state.saveAvailable.not() || state.newGroups.isEmpty()) return

        viewModelScope.launch(Dispatchers.Default) {
            toggleLoading(true)
            meetingsRepository.saveGroups(meetingId, state.newGroups)
                .onSuccess {
                    updateState()
                }
                .onFailure {
                    toggleLoading(false)
                }
        }
    }

    private fun toggleLoading(value: Boolean) {
        _screenState.update {
            (it as? State.Success)?.data?.let { state ->
                State.Success(state.copy(loading = value))
            } ?: it
        }
    }

    private fun List<Participant>.membersWithoutGroup(groups: List<Group>) =
        filter { member ->
            groups.none { it.members.contains(member.email) }
        }.toGroupMembers()
}