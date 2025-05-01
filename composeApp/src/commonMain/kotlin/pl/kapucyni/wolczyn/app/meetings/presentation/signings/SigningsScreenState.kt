package pl.kapucyni.wolczyn.app.meetings.presentation.signings

import dev.gitlive.firebase.firestore.Timestamp
import pl.kapucyni.wolczyn.app.meetings.domain.model.Meeting
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

data class SigningsScreenState(
    val loading: Boolean = false,
    val saveSuccess: Boolean = false,
    val meeting: Meeting,
    val isEditing: Boolean,
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
    val availableTypes: List<ParticipantType>,
    val type: ParticipantType?,
    val typeError: Boolean = false,
    val availableWorkshops: List<String>,
    val selectedWorkshop: String?,
    val workshopError: Boolean = false,
    val statuteChecked: Boolean = false,
    val contactNumber: String = "",
    val consentChecked: Boolean = false,
    val underageConsentChecked: Boolean = false,
    val successDialogVisible: Boolean = false,
    val noInternetDialogVisible: Boolean = false,
    val createdAt: Timestamp? = null,
)