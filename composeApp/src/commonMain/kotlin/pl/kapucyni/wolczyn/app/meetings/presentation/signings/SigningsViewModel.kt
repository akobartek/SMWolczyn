package pl.kapucyni.wolczyn.app.meetings.presentation.signings

import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.DataSaveError
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.MeetingSigningRemoved
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.MeetingSigningSaved
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent.MeetingSigningUpdated
import pl.kapucyni.wolczyn.app.common.utils.getPeselBeginning
import pl.kapucyni.wolczyn.app.common.utils.isAgeBelow
import pl.kapucyni.wolczyn.app.common.utils.isValidEmail
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.*

class SigningsViewModel(
    private val meetingId: Int,
    private val user: User?,
    private val email: String?,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<SigningsScreenState>() {

    init {
        viewModelScope.launch(Dispatchers.Default) {
            runCatching {
                meetingsRepository.getParticipantFlow(
                    meetingId = meetingId,
                    email = (user?.email ?: email).orEmpty(),
                )
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { participant ->
                        loadState(participant)
                    }
            }
        }
    }

    fun handleAction(action: SigningsAction) {
        when (action) {
            is UpdateFirstName -> updateFirstName(action.firstName)
            is UpdateLastName -> updateLastName(action.lastName)
            is UpdateCity -> updateCity(action.city)
            is UpdateBirthday -> updateBirthdayDate(action.millis)
            is UpdateEmail -> updateEmail(action.email)
            is UpdatePesel -> updatePesel(action.pesel)
            is UpdateType -> updateType(action.type)
            is UpdateWorkshop -> updateWorkshop(action.workshop)
            is UpdateStatuteConsent -> updateStatuteConsent(action.checked)
            is SaveData -> saveData()
            is RemoveSigning -> removeSigning()
            is HideSuccessDialog -> toggleSuccessDialog(visible = false)
            is HideTooYoungDialog -> hideTooYoungDialog()
            is HideNoInternetDialog -> hideNoInternetDialog()
        }
    }

    private fun loadState(participant: Participant?) {
        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                val meeting = async { meetingsRepository.getMeeting(meetingId) }
                val workshops = async { meetingsRepository.getAvailableWorkshops() }
                val group = async {
                    participant?.let {
                        meetingsRepository.getParticipantGroup(
                            meetingId = meetingId,
                            email = it.email,
                        )
                    }
                }
                val birthday = (participant?.birthday ?: user?.birthday)?.toMilliseconds()?.toLong()

                val state = State.Success(
                    data = SigningsScreenState(
                        meeting = meeting.await(),
                        isEditing = participant != null,
                        isSigningByAdmin = user == null,
                        isConfirmed = participant?.paid == true,
                        firstName = participant?.firstName ?: user?.firstName.orEmpty(),
                        lastName = participant?.lastName ?: user?.lastName.orEmpty(),
                        city = participant?.city ?: user?.city.orEmpty(),
                        email = participant?.email ?: user?.email.orEmpty(),
                        pesel =
                            participant?.pesel ?: user?.birthday?.getPeselBeginning().orEmpty(),
                        peselIsWoman = participant?.pesel?.peselIsWoman() == true,
                        birthdayDate = birthday,
                        isUnderAge = birthday?.isAgeBelow(age = 18) == true,
                        availableTypes = getAvailableTypes(birthday),
                        type = participant?.type,
                        availableWorkshops = workshops.await().map { it.name },
                        workshopsEnabled =
                            workshopsEnabled(participant?.type, participant?.pesel.orEmpty()),
                        selectedWorkshop = participant?.workshop,
                        createdAt = participant?.createdAt,
                        statuteChecked = participant != null,
                        group = group.await(),
                    )
                )
                _screenState.update { state }
            }
        }
    }

    private fun getAvailableTypes(birthday: Long?) = ParticipantType.entries.let { list ->
        when {
            user == null -> list

            birthday?.isAgeBelow(18) == true ->
                listOf(ParticipantType.MEMBER, ParticipantType.SCOUT)

            else -> list.filter { it.isSelectableByUser() }
        }
    }

    private fun updateFirstName(firstName: String) {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(data.copy(firstName = firstName, firstNameError = false))
        }
    }

    private fun updateLastName(lastName: String) {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(data.copy(lastName = lastName, lastNameError = false))
        }
    }

    private fun updateCity(city: String) {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(data.copy(city = city, cityError = false))
        }
    }

    private fun updateBirthdayDate(value: Long) {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(
                data.copy(
                    birthdayDate = value,
                    birthdayError = value > Clock.System.now().toEpochMilliseconds(),
                    isUnderAge = value.isAgeBelow(18),
                    pesel = value.getPeselBeginning(),
                    availableTypes = getAvailableTypes(value),
                )
            )
        }
    }

    private fun updateEmail(value: String) {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(data.copy(email = value, emailError = false))
        }
    }

    private fun updatePesel(pesel: String) {
        val data = (screenState.value as? State.Success)?.data ?: return
        if (
            pesel.startsWith(data.birthdayDate?.getPeselBeginning().orEmpty()).not()
            || pesel.length > 11
        ) return

        _screenState.update {
            State.Success(
                data.copy(
                    pesel = pesel,
                    peselError = false,
                    peselIsWoman = pesel.peselIsWoman(),
                    selectedWorkshop = null,
                    workshopsEnabled = workshopsEnabled(data.type, pesel),
                    workshopError = false,
                )
            )
        }
    }

    private fun updateType(type: ParticipantType) {
        val data = (screenState.value as? State.Success)?.data ?: return
        val workshopsEnabled = workshopsEnabled(type, data.pesel)
        _screenState.update {
            State.Success(
                data.copy(
                    type = type,
                    typeError = false,
                    selectedWorkshop = if (workshopsEnabled) data.selectedWorkshop else null,
                    workshopsEnabled = workshopsEnabled,
                    workshopError = data.workshopError && workshopsEnabled,
                )
            )
        }
    }

    private fun updateWorkshop(workshop: String) {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(data.copy(selectedWorkshop = workshop, workshopError = false))
        }
    }

    private fun updateStatuteConsent(checked: Boolean) {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(data.copy(statuteChecked = checked))
        }
    }

    private fun toggleSuccessDialog(visible: Boolean) {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(data.copy(successDialogVisible = visible))
        }
    }

    private fun hideTooYoungDialog() {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(data.copy(tooYoungDialogVisible = false))
        }
    }

    private fun hideNoInternetDialog() {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(data.copy(noInternetDialogVisible = false))
        }
    }

    private fun saveData() {
        val state = (screenState.value as? State.Success)?.data ?: return
        if (validateInput(state).not() || (state.statuteChecked.not() && user != null))
            return

        toggleLoading(state, true)
        viewModelScope.launch(Dispatchers.Default) {
            val result = meetingsRepository.saveParticipant(
                meetingId = meetingId,
                participant = Participant(
                    userId = user?.id.orEmpty(),
                    type = state.type ?: ParticipantType.MEMBER,
                    firstName = state.firstName,
                    lastName = state.lastName,
                    city = state.city,
                    email = state.email,
                    pesel = state.pesel,
                    contactNumber = state.contactNumber,
                    workshop = state.selectedWorkshop.orEmpty(),
                    birthday = state.birthdayDate?.let {
                        Timestamp.fromMilliseconds(it.toDouble())
                    } ?: Timestamp.now(),
                    createdAt = state.createdAt ?: Timestamp.now(),
                    paid = state.isConfirmed,
                    consents = state.consentChecked,
                    underageConsents = state.underageConsentChecked,
                )
            )
            toggleLoading(state, false)
            result
                .onSuccess {
                    when {
                        state.createdAt != null -> {
                            SnackbarController.sendEvent(event = MeetingSigningUpdated)
                        }

                        user == null -> {
                            SnackbarController.sendEvent(event = MeetingSigningSaved)
                            _screenState.update { State.Success(state.copy(operationFinished = true)) }
                        }

                        else -> {
                            toggleSuccessDialog(visible = true)
                        }
                    }
                }
                .onFailure { SnackbarController.sendEvent(DataSaveError) }
        }
    }

    private fun removeSigning() {
        val state = (screenState.value as? State.Success)?.data ?: return

        toggleLoading(state, true)
        viewModelScope.launch {
            meetingsRepository.removeParticipant(meetingId, state.email)
                .onSuccess {
                    SnackbarController.sendEvent(event = MeetingSigningRemoved)
                    _screenState.update { State.Success(state.copy(operationFinished = true)) }
                }
                .onFailure { SnackbarController.sendEvent(DataSaveError) }
        }
        toggleLoading(state, false)
    }

    private fun toggleLoading(state: SigningsScreenState, value: Boolean) =
        _screenState.update { State.Success(state.copy(loading = value)) }

    private fun validateInput(state: SigningsScreenState): Boolean {
        val newState = with(state) {
            copy(
                email = email.trim(),
                emailError =
                    email.trim().isValidEmail().not() || user?.let { it.email != email } == true,
                firstName = firstName.trim(),
                firstNameError = firstName.trim().isBlank(),
                lastName = lastName.trim(),
                lastNameError = lastName.trim().isBlank(),
                city = city.trim(),
                cityError = city.trim().isBlank(),
                birthdayError =
                    birthdayDate == null || birthdayDate > Clock.System.now().toEpochMilliseconds(),
                pesel = pesel.trim(),
                peselError = pesel.trim().isValidPesel().not(),
                typeError = type == null,
                workshopError = when {
                    workshopsEnabled(type, pesel).not() -> false
                    selectedWorkshop == null -> true
                    selectedWorkshop == COSMETIC_WORKSHOP -> pesel.peselIsWoman().not()
                    else -> false
                },
                tooYoungDialogVisible = isSigningByAdmin.not() && birthdayDate?.isAgeBelow(
                    age = 15,
                    other = Instant.fromEpochMilliseconds(meeting.start.toMilliseconds().toLong()),
                ) == true,
            )
        }
        _screenState.update { State.Success(newState) }
        return newState.emailError.not()
                && newState.firstNameError.not()
                && newState.lastNameError.not()
                && newState.cityError.not()
                && newState.birthdayError.not()
                && newState.peselError.not()
                && newState.typeError.not()
                && newState.workshopError.not()
                && newState.tooYoungDialogVisible.not()
    }

    private fun CharSequence.isValidPesel(): Boolean {
        val regex = Regex("\\b[0-9]{2}([02468][1-9]|[13579][0-2])(0[1-9]|[1,2][0-9]|3[0-1])\\d{5}")
        return this.matches(regex) && peselControlNumberValidation()
    }

    private fun CharSequence.peselControlNumberValidation(): Boolean = try {
        map { it.digitToInt() }.let { digits ->
            val wages = listOf(1, 3, 7, 9, 1, 3, 7, 9, 1, 3)
            val controlNumber = digits.last()
            val calculatedNumber = digits.take(10)
                .zip(wages)
                .sumOf { (digit, wage) -> digit * wage % 10 }
                .let { sum -> (10 - sum % 10) % 10 }
            controlNumber == calculatedNumber
        }
    } catch (_: Exception) {
        false
    }

    private fun workshopsEnabled(type: ParticipantType?, pesel: String) =
        type?.canSelectWorkshops() == true && pesel.isValidPesel()

    private fun CharSequence.peselIsWoman() = getOrNull(9)?.let {
        it.isDigit() && (it.digitToInt()) % 2 == 0
    } == true

    companion object {
        const val COSMETIC_WORKSHOP = "Kosmetyczne"
    }
}