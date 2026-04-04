package pl.kapucyni.wolczyn.app.meetings.presentation.signings.user

import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

sealed class SigningsState(val qrEmail: String?) {
    data class Confirmed(
        val firstName: String,
        val email: String,
        val group: Group?,
    ): SigningsState(qrEmail = email)

    data class NotConfirmed(
        val essentialsUrl: String,
        val statuteUrl: String,
        val parentAgreementUrl: String,
        val isEditing: Boolean,
        val firstName: String,
        val firstNameError: Boolean = false,
        val lastName: String,
        val lastNameError: Boolean = false,
        val city: String,
        val cityError: Boolean = false,
        val contactNumber: String = "",
        val contactNumberError: Boolean = false,
        val email: String,
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
        val notesEnabled: Boolean = false,
        val notes: String = "",
        val notesError: Boolean = false,
        val statuteChecked: Boolean,
        val consentChecked: Boolean = false,
        val underageConsentChecked: Boolean = false,
        val successDialogVisible: Boolean = false,
        val tooYoungDialogVisible: Boolean = false,
        val noInternetDialogVisible: Boolean = false,
    ): SigningsState(qrEmail = email.takeIf { isEditing })
}