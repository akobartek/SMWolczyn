package pl.kapucyni.wolczyn.app.meetings.presentation.signings

import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarController
import pl.kapucyni.wolczyn.app.common.presentation.snackbars.SnackbarEvent
import pl.kapucyni.wolczyn.app.common.utils.getPeselBeginning
import pl.kapucyni.wolczyn.app.common.utils.isValidEmail
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.*

class SigningsViewModel(
    private val meetingId: Int,
    private val user: User?,
    private val meetingsRepository: MeetingsRepository,
) : BasicViewModel<SigningsScreenState>() {

    init {
        viewModelScope.launch {
            coroutineScope {
                val meeting = async { meetingsRepository.getMeeting(meetingId) }
                val workshops = async { meetingsRepository.getAvailableWorkshops() }
                val previousSigning = async {
                    user?.let { meetingsRepository.checkPreviousSigning(meetingId, it.email) }
                }

                previousSigning.await().let { participant ->
                    val state = State.Success(
                        data = SigningsScreenState(
                            meeting = meeting.await(),
                            isEditing = participant != null,
                            isUserInfoEditable = user == null,
                            firstName = participant?.firstName ?: user?.firstName.orEmpty(),
                            lastName = participant?.lastName ?: user?.lastName.orEmpty(),
                            city = participant?.city ?: user?.city.orEmpty(),
                            email = participant?.email ?: user?.email.orEmpty(),
                            pesel =
                                participant?.pesel ?: user?.birthday?.getPeselBeginning().orEmpty(),
                            birthdayDate = (participant?.birthday ?: user?.birthday)
                                ?.toMilliseconds()?.toLong(),
                            availableTypes = ParticipantType.entries.let {
                                when {
                                    user == null -> it

                                    user.isUnderAge() ->
                                        listOf(ParticipantType.MEMBER, ParticipantType.SCOUT)

                                    else -> it - ParticipantType.ORGANISATION
                                }
                            },
                            type = participant?.type,
                            workshopsVisible = participant?.type?.canSelectWorkshops() ?: false,
                            availableWorkshops = workshops.await().map { it.name },
                            selectedWorkshop = participant?.workshop,
                            createdAt = participant?.createdAt,
                        )
                    )
                    _screenState.update { state }
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
            is SaveData -> saveData()
            is ToggleNoInternetDialog -> hideNoInternetDialog()
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
                    birthdayError = false,
                    pesel = value.getPeselBeginning(),
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
        user?.let {
            if (pesel.startsWith(user.birthday.getPeselBeginning()).not()) return
        }
        _screenState.update {
            State.Success(data.copy(pesel = pesel, peselError = false))
        }
    }

    private fun updateType(type: ParticipantType) {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(
                data.copy(
                    type = type,
                    typeError = false,
                    workshopsVisible = type.canSelectWorkshops(),
                    selectedWorkshop = if (type.canSelectWorkshops()) data.selectedWorkshop else null,
                    workshopError = data.workshopError && type.canSelectWorkshops(),
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

    private fun hideNoInternetDialog() {
        val data = (screenState.value as? State.Success)?.data ?: return
        _screenState.update {
            State.Success(data.copy(noInternetDialogVisible = false))
        }
    }

    private fun saveData() {
        if (validateInput().not()) return
        val state = (screenState.value as? State.Success)?.data ?: return

        toggleLoading(state, true)
        viewModelScope.launch {
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
                    consents = state.consentChecked,
                    underageConsents = state.underageConsentChecked,
                )
            )
            toggleLoading(state, false)
            result
                .onSuccess {
                    SnackbarController.sendEvent(
                        event = state.createdAt?.let { SnackbarEvent.MeetingSigningUpdated }
                            ?: SnackbarEvent.MeetingSigningSaved,
                    )
                    _screenState.update { State.Success(state.copy(saveSuccess = true)) }
                }
                .onFailure { SnackbarController.sendEvent(SnackbarEvent.DataSaveError) }
        }
    }

    private fun toggleLoading(state: SigningsScreenState, value: Boolean) =
        _screenState.update { State.Success(state.copy(loading = value)) }

    private fun validateInput(): Boolean {
        val state = (screenState.value as? State.Success)?.data ?: return false
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
                birthdayError = birthdayDate == null,
                pesel = pesel.trim(),
                peselError = pesel.trim().isValidPesel().not(),
                typeError = type == null,
                workshopError = type?.canSelectWorkshops() == true && selectedWorkshop == null,
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
                .let { sum -> 10 - sum % 10 }
            controlNumber == calculatedNumber
        }
    } catch (exc: Exception) {
        false
    }

}