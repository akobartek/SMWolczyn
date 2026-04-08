package pl.kapucyni.wolczyn.app.meetings.presentation.signings.user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.DataSaveError
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.MeetingSigningRemoved
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.MeetingSigningUpdated
import pl.kapucyni.wolczyn.app.common.utils.getPeselBeginning
import pl.kapucyni.wolczyn.app.common.utils.isAgeBelow
import pl.kapucyni.wolczyn.app.common.utils.isValidPesel
import pl.kapucyni.wolczyn.app.common.utils.isValidPhoneNumber
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Gender
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.HideNoInternetDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.HideSuccessDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.HideTooYoungDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.RemoveSigning
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.SaveData
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateAnimatorInfoChecked
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateContactNumber
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateCity
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateLastName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateNotes
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdatePesel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateStatuteConsent
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsEvent.NavigateUp
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsEvent.UserNotAvailable
import kotlin.time.Clock
import kotlin.time.Instant

internal const val PHONE_CODE = "+48"

class SigningsViewModel(
    savedStateHandle: SavedStateHandle,
    authRepository: AuthRepository,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<SigningsState>() {

    private val args = savedStateHandle.toRoute<Screen.Signings>()

    private val _events = Channel<SigningsEvent>()
    val events = _events.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentUserData = authRepository.currentUser.flatMapLatest { user ->
        user?.let {
            meetingsRepository.getParticipantFlow(
                meetingId = args.meetingId,
                email = user.email,
            ).map { participant ->
                user to participant
            }
        } ?: run {
            _events.trySend(UserNotAvailable)
            flowOf(null)
        }
    }.onEach { data ->
        if (data != null) {
            val (user, participant) = data
            updateState(user, participant)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = null)

    fun handleAction(action: SigningsAction) {
        when (action) {
            is UpdateFirstName -> updateFirstName(action.firstName)
            is UpdateLastName -> updateLastName(action.lastName)
            is UpdateCity -> updateCity(action.city)
            is UpdateContactNumber -> updateContactNumber(action.contactNumber)
            is UpdateBirthday -> updateBirthdayDate(action.millis)
            is UpdatePesel -> updatePesel(action.pesel)
            is UpdateType -> updateType(action.type)
            is UpdateWorkshop -> updateWorkshop(action.workshop)
            is UpdateNotes -> updateNotes(action.notes)
            is UpdateStatuteConsent -> updateStatuteConsent(action.checked)
            is UpdateAnimatorInfoChecked -> updateAnimatorInfoChecked(action.checked)
            is SaveData -> saveData()
            is RemoveSigning -> removeSigning()
            is HideSuccessDialog -> toggleSuccessDialog(visible = false)
            is HideTooYoungDialog -> hideTooYoungDialog()
            is HideNoInternetDialog -> hideNoInternetDialog()
        }
    }

    private fun updateState(user: User, participant: Participant?) {
        viewModelScope.launch(Dispatchers.Default) {
            val state = if (participant?.paid == true) {
                val group = participant.let {
                    meetingsRepository.getParticipantGroup(args.meetingId, it.email)
                }
                SigningsState.Confirmed(
                    firstName = participant.firstName,
                    email = participant.email,
                    group = group,
                )
            } else {
                val currentState = state.value as? SigningsState.NotConfirmed
                val workshops = meetingsRepository.getAvailableWorkshops(args.meetingId)
                val meeting = meetingsRepository.getMeeting(args.meetingId)
                val birthday = (participant?.birthday ?: user.birthday)?.toMilliseconds()?.toLong()

                SigningsState.NotConfirmed(
                    essentialsUrl = meeting.essentialsUrl,
                    statuteUrl = meeting.statuteUrl,
                    parentAgreementUrl = meeting.parentAgreementUrl,
                    isEditing = participant != null,
                    firstName = participant?.firstName ?: user.firstName,
                    lastName = participant?.lastName ?: user.lastName,
                    city = participant?.city ?: user.city,
                    contactNumber = participant?.contactNumber.orEmpty().removePrefix(PHONE_CODE),
                    email = participant?.email ?: user.email,
                    pesel = participant?.pesel ?: user.birthday?.getPeselBeginning().orEmpty(),
                    birthdayDate = birthday,
                    isUnderAge = birthday?.isAgeBelow(age = 18) == true,
                    availableTypes = getAvailableTypes(birthday),
                    type = participant?.type,
                    allWorkshops = workshops,
                    availableWorkshops = workshops.map { it.name },
                    workshopsEnabled =
                        workshopsEnabled(participant?.type, participant?.pesel.orEmpty()),
                    selectedWorkshop = participant?.workshop,
                    notes = participant?.notes.orEmpty(),
                    notesEnabled = participant?.type?.notesAvailable() == true,
                    statuteChecked = participant != null,
                    animatorInfoChecked = participant?.type == ParticipantType.ANIMATOR,
                    successDialogVisible = currentState?.successDialogVisible == true,
                    tooYoungDialogVisible = currentState?.tooYoungDialogVisible == true,
                    noInternetDialogVisible = currentState?.noInternetDialogVisible == true,
                )
            }
            _state.update { state }
        }
    }

    private fun getAvailableTypes(birthday: Long?) = ParticipantType.entries.let { list ->
        when {
            birthday?.isAgeBelow(18) == true ->
                listOf(ParticipantType.MEMBER, ParticipantType.SCOUT)

            else -> list.filter { it.isSelectableByUser() }
        }
    }

    private fun updateFirstName(firstName: String) {
        _state.update {
            (it as? SigningsState.NotConfirmed)?.copy(firstName = firstName, firstNameError = false)
        }
    }

    private fun updateLastName(lastName: String) {
        _state.update {
            (it as? SigningsState.NotConfirmed)?.copy(lastName = lastName, lastNameError = false)
        }
    }

    private fun updateCity(city: String) {
        _state.update {
            (it as? SigningsState.NotConfirmed)?.copy(city = city, cityError = false)
        }
    }

    private fun updateContactNumber(contactNumber: String) {
        _state.update {
            (it as? SigningsState.NotConfirmed)?.copy(
                contactNumber = contactNumber,
                contactNumberError = false,
            )
        }
    }

    private fun updateBirthdayDate(value: Long) {
        _state.update {
            (it as? SigningsState.NotConfirmed)?.copy(
                birthdayDate = value,
                birthdayError = value > Clock.System.now().toEpochMilliseconds(),
                isUnderAge = value.isAgeBelow(18),
                pesel = value.getPeselBeginning(),
                availableTypes = getAvailableTypes(value),
            )
        }
    }

    private fun updatePesel(pesel: String) {
        val birthdayDate = (_state.value as? SigningsState.NotConfirmed)?.birthdayDate
        if (
            pesel.startsWith(birthdayDate?.getPeselBeginning().orEmpty()).not()
            || pesel.length > 11
        ) return
        val state = (_state.value as? SigningsState.NotConfirmed) ?: return
        val workshops =
            if (pesel.length < 11) listOf()
            else state.allWorkshops
                .filter { it.allow(pesel.genderByPesel()) }
                .map { it.name }

        _state.update {
            state.copy(
                pesel = pesel,
                peselError = false,
                selectedWorkshop = null,
                availableWorkshops = workshops,
                workshopsEnabled = workshopsEnabled(state.type, pesel),
                workshopError = false,
            )
        }
    }

    private fun updateType(type: ParticipantType) {
        _state.update {
            (it as? SigningsState.NotConfirmed)?.let { state ->
                val workshopsEnabled = workshopsEnabled(type, state.pesel)
                state.copy(
                    type = type,
                    typeError = false,
                    selectedWorkshop = if (workshopsEnabled) it.selectedWorkshop else null,
                    workshopsEnabled = workshopsEnabled,
                    workshopError = it.workshopError && workshopsEnabled,
                    notes = if (type.notesAvailable()) state.notes else "",
                    notesEnabled = type.notesAvailable(),
                    animatorInfoChecked = false,
                )
            }
        }
    }

    private fun updateWorkshop(workshop: String) {
        _state.update {
            (it as? SigningsState.NotConfirmed)?.copy(
                selectedWorkshop = workshop,
                workshopError = false
            )
        }
    }

    private fun updateNotes(notes: String) {
        _state.update {
            (it as? SigningsState.NotConfirmed)?.copy(
                notes = notes,
                notesError = false,
            )
        }
    }

    private fun updateStatuteConsent(checked: Boolean) {
        _state.update { (it as? SigningsState.NotConfirmed)?.copy(statuteChecked = checked) }
    }

    private fun updateAnimatorInfoChecked(checked: Boolean) {
        _state.update { (it as? SigningsState.NotConfirmed)?.copy(animatorInfoChecked = checked) }
    }

    private fun toggleSuccessDialog(visible: Boolean) {
        _state.update { (it as? SigningsState.NotConfirmed)?.copy(successDialogVisible = visible) }
        if (visible.not())
            _events.trySend(NavigateUp)
    }

    private fun hideTooYoungDialog() {
        _state.update { (it as? SigningsState.NotConfirmed)?.copy(tooYoungDialogVisible = false) }
        _events.trySend(NavigateUp)
    }

    private fun hideNoInternetDialog() {
        _state.update { (it as? SigningsState.NotConfirmed)?.copy(noInternetDialogVisible = false) }
    }

    private fun saveData() {
        val state = state.value as? SigningsState.NotConfirmed ?: return
        val (user, participant) = currentUserData.value ?: return

        viewModelScope.launch(Dispatchers.Default) {
            if (validateInput(state).not() || state.statuteChecked.not())
                return@launch

            setLoading(true)
            meetingsRepository.saveParticipant(
                meetingId = args.meetingId,
                participant = Participant(
                    userId = user.id,
                    type = state.type ?: ParticipantType.MEMBER,
                    firstName = state.firstName.trim(),
                    lastName = state.lastName.trim(),
                    city = state.city.trim(),
                    email = state.email.trim(),
                    pesel = state.pesel.trim(),
                    contactNumber = "$PHONE_CODE${state.contactNumber.trim()}",
                    workshop = state.selectedWorkshop.orEmpty(),
                    birthday = state.birthdayDate?.let {
                        Timestamp.fromMilliseconds(it.toDouble())
                    } ?: Timestamp.now(),
                    notes = state.notes,
                    createdAt = participant?.createdAt ?: Timestamp.now(),
                    paid = participant?.paid == true,
                    consents = participant?.consents == true,
                    underageConsents = participant?.underageConsents == true,
                )
            ).onSuccess {
                setLoading(false)
                if (state.isEditing)
                    SnackbarController.sendEvent(event = MeetingSigningUpdated)
                else
                    toggleSuccessDialog(visible = true)
            }.onFailure {
                setLoading(false)
                SnackbarController.sendEvent(DataSaveError)
            }
        }
    }

    private suspend fun validateInput(state: SigningsState.NotConfirmed): Boolean {
        val meeting = meetingsRepository.getMeeting(args.meetingId)
        val newState = with(state) {
            copy(
                firstName = firstName.trim(),
                firstNameError = firstName.trim().isBlank(),
                lastName = lastName.trim(),
                lastNameError = lastName.trim().isBlank(),
                city = city.trim(),
                cityError = city.trim().isBlank(),
                contactNumber = contactNumber.trim(),
                contactNumberError = contactNumber.isValidPhoneNumber().not(),
                birthdayError =
                    birthdayDate == null || birthdayDate > Clock.System.now().toEpochMilliseconds(),
                pesel = pesel.trim(),
                peselError = pesel.trim().isValidPesel().not(),
                typeError = type == null,
                workshopError = when {
                    workshopsEnabled.not() -> false
                    selectedWorkshop == null -> true
                    allWorkshops.firstOrNull { it.name == selectedWorkshop }
                        ?.allow(pesel.genderByPesel()) != true -> true

                    else -> false
                },
                notes = notes.trim(),
                notesError =
                    if (notesEnabled) notes.trim().length < 10
                    else false,
                tooYoungDialogVisible = birthdayDate?.isAgeBelow(
                    age = 15,
                    other = Instant.fromEpochMilliseconds(meeting.start.toMilliseconds().toLong()),
                ) == true,
            )
        }
        _state.update { newState }
        return newState.firstNameError.not()
                && newState.lastNameError.not()
                && newState.cityError.not()
                && newState.birthdayError.not()
                && newState.peselError.not()
                && newState.typeError.not()
                && newState.workshopError.not()
                && newState.notesError.not()
                && newState.tooYoungDialogVisible.not()
    }

    private fun removeSigning() {
        val state = _state.value as? SigningsState.NotConfirmed ?: return
        setLoading(true)
        viewModelScope.launch {
            meetingsRepository.removeParticipant(args.meetingId, state.email)
                .onSuccess {
                    setLoading(false)
                    SnackbarController.sendEvent(event = MeetingSigningRemoved)
                    _events.send(NavigateUp)
                }
                .onFailure {
                    setLoading(false)
                    SnackbarController.sendEvent(DataSaveError)
                }
        }
    }

    private fun workshopsEnabled(type: ParticipantType?, pesel: String) =
        type?.canSelectWorkshops() == true && pesel.isValidPesel()

    private fun CharSequence.genderByPesel() = getOrNull(9)?.let {
        when {
            it.isDigit().not() -> Gender.BOTH
            (it.digitToInt()) % 2 == 0 -> Gender.FEMALE
            else -> Gender.MALE
        }
    } ?: Gender.BOTH
}