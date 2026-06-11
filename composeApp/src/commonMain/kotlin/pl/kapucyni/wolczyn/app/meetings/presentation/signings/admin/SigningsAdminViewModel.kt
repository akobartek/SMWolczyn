package pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.AuthRepository
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.navigation.ParticipantParameterType
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.DataSaveError
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.MeetingSigningRemoved
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.MeetingSigningSaved
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.MeetingSigningUpdated
import pl.kapucyni.wolczyn.app.common.utils.getPeselBeginning
import pl.kapucyni.wolczyn.app.common.utils.isAgeBelow
import pl.kapucyni.wolczyn.app.common.utils.isValidEmail
import pl.kapucyni.wolczyn.app.common.utils.isValidPesel
import pl.kapucyni.wolczyn.app.common.utils.isValidPhoneNumber
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.HideNoInternetDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.OnBackPressed
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.RemoveSigning
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.SaveData
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateContactNumber
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateCity
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateCommunity
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateEmail
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateLastName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateNotes
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdatePesel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminEvent.NavigateUp
import kotlin.reflect.typeOf
import kotlin.time.Clock

class SigningsAdminViewModel(
    savedStateHandle: SavedStateHandle,
    private val meetingsRepository: MeetingsRepository,
    private val authRepository: AuthRepository,
) : BasicViewModel<SigningsAdminState>() {

    private val args = savedStateHandle.toRoute<Screen.SigningsAdmin>(
        typeMap = mapOf(typeOf<Participant?>() to ParticipantParameterType),
    )

    private val _events = Channel<SigningsAdminEvent>()
    val events = _events.receiveAsFlow()

    init {
        initState()
    }

    fun handleAction(action: SigningsAdminAction) {
        when (action) {
            is OnBackPressed -> onBackPressed()
            is UpdateFirstName -> updateFirstName(action.firstName)
            is UpdateLastName -> updateLastName(action.lastName)
            is UpdateCity -> updateCity(action.city)
            is UpdateContactNumber -> updateContactNumber(action.contactNumber)
            is UpdateBirthday -> updateBirthdayDate(action.millis)
            is UpdateEmail -> updateEmail(action.email)
            is UpdatePesel -> updatePesel(action.pesel)
            is UpdateCommunity -> updateCommunity(action.community)
            is UpdateType -> updateType(action.type)
            is UpdateWorkshop -> updateWorkshop(action.workshop)
            is UpdateNotes -> updateNotes(action.notes)
            is SaveData -> saveData()
            is RemoveSigning -> removeSigning()
            is HideNoInternetDialog -> hideNoInternetDialog()
        }
    }

    private fun initState() = viewModelScope.launch(Dispatchers.Default) {
        val workshops = meetingsRepository.getAllWorkshops(args.meetingId)
        val state = (
                args.participant?.let { participant ->
                    SigningsAdminState.fromParticipant(participant)
                } ?: SigningsAdminState()
                ).copy(availableWorkshops = workshops.map { it.name })
        _state.update { state }
    }

    private fun onBackPressed() {
        viewModelScope.launch { _events.send(NavigateUp) }
    }

    private fun updateFirstName(firstName: String) {
        _state.update { it?.copy(firstName = firstName, firstNameError = false) }
    }

    private fun updateLastName(lastName: String) {
        _state.update { it?.copy(lastName = lastName, lastNameError = false) }
    }

    private fun updateCity(city: String) {
        _state.update { it?.copy(city = city, cityError = false) }
    }

    private fun updateContactNumber(contactNumber: String) {
        _state.update { it?.copy(contactNumber = contactNumber, contactNumberError = false) }
    }

    private fun updateBirthdayDate(value: Long) {
        _state.update {
            it?.copy(
                birthdayDate = value,
                birthdayError = value > Clock.System.now().toEpochMilliseconds(),
                isUnderAge = value.isAgeBelow(18),
                pesel = value.getPeselBeginning(),
            )
        }
    }

    private fun updateEmail(email: String) {
        _state.update { it?.copy(email = email, emailError = false) }
    }

    private fun updatePesel(pesel: String) {
        val birthdayDate = _state.value?.birthdayDate
        if (
            pesel.startsWith(birthdayDate?.getPeselBeginning().orEmpty()).not()
            || pesel.length > 11
        ) return

        _state.update {
            it?.copy(
                pesel = pesel,
                peselError = false,
                selectedWorkshop = null,
                workshopsEnabled = workshopsEnabled(it.type, pesel),
                workshopError = false,
            )
        }
    }

    private fun updateCommunity(community: String) {
        _state.update { it?.copy(community = community) }
    }

    private fun updateType(type: ParticipantType) {
        _state.update {
            val workshopsEnabled = workshopsEnabled(type, it?.pesel.orEmpty())
            it?.copy(
                type = type,
                typeError = false,
                selectedWorkshop = if (workshopsEnabled) it.selectedWorkshop else null,
                workshopsEnabled = workshopsEnabled,
                workshopError = it.workshopError && workshopsEnabled,
                notes = if (type.notesAvailable()) it.notes else "",
                notesEnabled = type.notesAvailable(),
            )
        }
    }

    private fun updateWorkshop(workshop: String) {
        _state.update { it?.copy(selectedWorkshop = workshop, workshopError = false) }
    }

    private fun updateNotes(notes: String) {
        _state.update { it?.copy(notes = notes, notesError = false) }
    }

    private fun hideNoInternetDialog() {
        _state.update { it?.copy(noInternetDialogVisible = false) }
    }

    private fun saveData() {
        val state = state.value ?: return
        if (validateInput(state).not())
            return

        setLoading(true)
        viewModelScope.launch(Dispatchers.Default) {
            val participant = args.participant
            val userId = participant?.userId
                ?: authRepository.getUserIdIfExists(state.email)
            meetingsRepository.saveParticipant(
                meetingId = args.meetingId,
                participant = (participant ?: Participant()).copy(
                    userId = userId,
                    type = state.type ?: ParticipantType.MEMBER,
                    firstName = state.firstName.trim(),
                    lastName = state.lastName.trim(),
                    city = state.city.trim(),
                    email = state.email.trim(),
                    pesel = state.pesel.trim(),
                    community = state.community.trim(),
                    contactNumber = "+48${state.contactNumber.trim()}",
                    workshop = state.selectedWorkshop.orEmpty(),
                    birthday = state.birthdayDate?.let {
                        Timestamp.fromMilliseconds(it.toDouble())
                    } ?: Timestamp.now(),
                    notes = state.notes,
                )
            ).onSuccess {
                setLoading(false)
                SnackbarController.sendEvent(
                    event = if (participant == null) MeetingSigningSaved else MeetingSigningUpdated,
                )
                _events.send(NavigateUp)
            }.onFailure {
                setLoading(false)
                SnackbarController.sendEvent(DataSaveError)
            }
        }
    }

    private fun validateInput(state: SigningsAdminState): Boolean {
        val newState = with(state) {
            copy(
                email = email.trim(),
                emailError = email.trim().isValidEmail().not(),
                firstName = firstName.trim(),
                firstNameError = firstName.trim().isBlank(),
                lastName = lastName.trim(),
                lastNameError = lastName.trim().isBlank(),
                city = city.trim(),
                cityError = city.trim().isBlank(),
                contactNumber = contactNumber.trim(),
                contactNumberError =
                    contactNumber.trim().isBlank().not() && contactNumber.isValidPhoneNumber().not(),
                birthdayError =
                    birthdayDate == null || birthdayDate > Clock.System.now().toEpochMilliseconds(),
                pesel = pesel.trim(),
                peselError = pesel.trim().isValidPesel().not(),
                typeError = type == null,
                workshopError = false,
                notes = notes.trim(),
            )
        }
        _state.update { newState }
        return newState.emailError.not()
                && newState.firstNameError.not()
                && newState.lastNameError.not()
                && newState.cityError.not()
                && newState.contactNumberError.not()
                && newState.birthdayError.not()
                && newState.peselError.not()
                && newState.typeError.not()
                && newState.workshopError.not()
    }

    private fun removeSigning() {
        val state = _state.value ?: return
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
}