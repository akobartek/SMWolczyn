package pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin

import dev.gitlive.firebase.firestore.toMilliseconds
import pl.kapucyni.wolczyn.app.common.utils.isAgeBelow
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

data class SigningsAdminState(
    val isEditing: Boolean = false,
    val firstName: String = "",
    val firstNameError: Boolean = false,
    val lastName: String = "",
    val lastNameError: Boolean = false,
    val city: String = "",
    val cityError: Boolean = false,
    val email: String = "",
    val emailEnabled: Boolean = true,
    val emailError: Boolean = false,
    val pesel: String = "",
    val peselError: Boolean = false,
    val birthdayDate: Long? = null,
    val birthdayError: Boolean = false,
    val isUnderAge: Boolean = false,
    val availableTypes: List<ParticipantType> = ParticipantType.entries,
    val type: ParticipantType? = null,
    val typeError: Boolean = false,
    val availableWorkshops: List<String> = listOf(),
    val selectedWorkshop: String? = null,
    val workshopsEnabled: Boolean = false,
    val workshopError: Boolean = false,
    val contactNumber: String = "",
    val noInternetDialogVisible: Boolean = false,
) {
    companion object {
        fun fromParticipant(participant: Participant) = with(participant) {
            val birthdayDate = birthday.toMilliseconds().toLong()
            SigningsAdminState().copy(
                isEditing = true,
                firstName = firstName,
                lastName = lastName,
                city = city,
                email = email,
                emailEnabled = false,
                pesel = pesel,
                birthdayDate = birthdayDate,
                isUnderAge = birthdayDate.isAgeBelow(age = 18),
                type = type,
                selectedWorkshop = workshop,
                workshopsEnabled = type.canSelectWorkshops(),
                contactNumber = contactNumber,
            )
        }
    }
}
