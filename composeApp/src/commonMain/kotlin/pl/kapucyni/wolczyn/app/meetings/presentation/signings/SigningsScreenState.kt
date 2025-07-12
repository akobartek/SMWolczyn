package pl.kapucyni.wolczyn.app.meetings.presentation.signings

import dev.gitlive.firebase.firestore.Timestamp
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.Meeting
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

data class SigningsScreenState(
    val loading: Boolean = false,
    val operationFinished: Boolean = false,
    val meeting: Meeting,
    val isEditing: Boolean,
    val isSigningByAdmin: Boolean,
    val isConfirmed: Boolean,
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
    val peselIsWoman: Boolean,
    val birthdayDate: Long?,
    val birthdayError: Boolean = false,
    val isUnderAge: Boolean,
    val availableTypes: List<ParticipantType>,
    val type: ParticipantType?,
    val typeError: Boolean = false,
    val availableWorkshops: List<String>,
    val selectedWorkshop: String?,
    val workshopsEnabled: Boolean = false,
    val workshopError: Boolean = false,
    val statuteChecked: Boolean,
    val contactNumber: String = "",
    val consentChecked: Boolean = false,
    val underageConsentChecked: Boolean = false,
    val successDialogVisible: Boolean = false,
    val tooYoungDialogVisible: Boolean = false,
    val noInternetDialogVisible: Boolean = false,
    val createdAt: Timestamp? = null,
    val group: Group?,
)