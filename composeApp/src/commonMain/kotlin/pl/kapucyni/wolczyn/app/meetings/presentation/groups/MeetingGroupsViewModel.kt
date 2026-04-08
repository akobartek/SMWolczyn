package pl.kapucyni.wolczyn.app.meetings.presentation.groups

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType.ANIMATOR
import pl.kapucyni.wolczyn.app.meetings.domain.model.toGroupMembers
import pl.kapucyni.wolczyn.app.meetings.domain.usecases.DrawGroupsUseCase
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.DrawGroups
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnAnimatorClicked
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnAnimatorDataChange
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnMemberGroupChange
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.SaveGroups
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.ToggleAnimatorsDialog
import kotlin.collections.contains

class MeetingGroupsViewModel(
    savedStateHandle: SavedStateHandle,
    private val meetingsRepository: MeetingsRepository,
    private val drawGroupsUseCase: DrawGroupsUseCase,
) : BasicViewModel<MeetingGroupsScreenState>() {

    private val meetingId = savedStateHandle.toRoute<Screen.MeetingGroups>().meetingId

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
                val selectedAnimators = savedGroups.takeIf { it.isNotEmpty() }?.let { groups ->
                    val currentAnimatorsEmails = groups.map { it.animatorMail }.toSet()
                    animators.filter { it.email in currentAnimatorsEmails }
                } ?: animators.filter { it.type == ANIMATOR }

                _state.update {
                    MeetingGroupsScreenState(
                        newGroups = savedGroups,
                        savedGroups = savedGroups,
                        participants = members,
                        membersWithoutGroup = members.membersWithoutGroup(savedGroups),
                        potentialAnimators = animators,
                        selectedAnimators = selectedAnimators,
                        copyAvailable = savedGroups.isNotEmpty(),
                        loading = false,
                    )
                }
            }
        }
    }

    private fun toggleAnimatorsDialog() {
        _state.update { it?.copy(animatorsDialogVisible = it.animatorsDialogVisible.not()) }
    }

    private fun onAnimatorClicked(animator: Participant) {
        _state.update {
            it?.let { state ->
                val animators = state.selectedAnimators
                state.copy(
                    selectedAnimators =
                        if (animators.contains(animator)) animators - animator
                        else animators + animator,
                )
            }
        }
    }

    private fun onAnimatorDataChange(currentGroup: Int, newGroup: Int, contact: String) {
        val state = _state.value ?: return
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

            _state.update { it?.copy(newGroups = newGroups, saveAvailable = true) }
        }
    }

    private fun onMemberGroupChange(currentGroup: Int?, newGroup: Int?, email: String) {
        if (newGroup == currentGroup) return
        val state = _state.value ?: return

        val currentGroupMembers = currentGroup?.let {
            state.newGroups.firstOrNull { it.number == currentGroup }?.members
        } ?: state.membersWithoutGroup
        val newGroupMembers = newGroup?.let {
            state.newGroups.firstOrNull { it.number == newGroup }?.members
        } ?: state.membersWithoutGroup

        val memberData = currentGroupMembers[email] ?: return
        val updatedCurrentMembers = currentGroupMembers.toMutableMap().apply { remove(email) }
        val updatedNewMembers = newGroupMembers.toMutableMap().apply { put(email, memberData) }

        val newMembersWithoutGroup = when {
            currentGroup != null && newGroup == null -> updatedNewMembers
            currentGroup == null && newGroup != null -> updatedCurrentMembers
            else -> state.membersWithoutGroup
        }
        val newGroups = state.newGroups.map { group ->
            when (group.number) {
                currentGroup -> group.copy(members = updatedCurrentMembers)
                newGroup -> group.copy(members = updatedNewMembers)
                else -> group
            }
        }

        _state.update {
            state.copy(
                membersWithoutGroup = newMembersWithoutGroup,
                newGroups = newGroups,
                saveAvailable = true,
            )
        }
    }

    private fun drawGroups() {
        val state = _state.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val groups = drawGroupsUseCase(
                participants = state.participants,
                animators = state.selectedAnimators,
            )
            _state.update {
                state.copy(
                    newGroups = groups,
                    membersWithoutGroup = state.participants.membersWithoutGroup(groups),
                    saveAvailable = true,
                )
            }
        }
    }

    private fun saveGroups() {
        val state = _state.value ?: return
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
        _state.update { it?.copy(loading = value) }
    }

    private fun List<Participant>.membersWithoutGroup(groups: List<Group>) =
        filter { member ->
            groups.none { it.members.contains(member.email) }
        }.toGroupMembers().toMutableMap()
}