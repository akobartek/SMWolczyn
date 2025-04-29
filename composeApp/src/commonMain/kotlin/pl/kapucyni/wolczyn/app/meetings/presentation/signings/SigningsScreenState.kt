package pl.kapucyni.wolczyn.app.meetings.presentation.signings

import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

data class SigningsScreenState(
    val loading: Boolean = false,
    val isUserInfoEditable: Boolean,
    val firstName: String,
    val firstNameError: Boolean = false,
    val lastName: String,
    val lastNameError: Boolean = false,
    val city: String,
    val cityError: Boolean = false,
    val email: String,
    val emailError: Boolean = false,
    val pesel: String,
    val peselError: Boolean = false,
    val birthdayDate: Long?,
    val birthdayError: Boolean = false,
    val type: ParticipantType?,
    val availableTypes: List<ParticipantType>,
    val selectedWorkshop: String?,
    val availableWorkshops: List<String>,
    val contactNumber: String = "",
    val consentChecked: Boolean = false,
    val underageConsentChecked: Boolean = false,
)