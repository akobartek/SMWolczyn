package pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin

import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

sealed interface SigningsAdminAction {
    data object OnBackPressed : SigningsAdminAction
    data class UpdateFirstName(val firstName: String) : SigningsAdminAction
    data class UpdateLastName(val lastName: String) : SigningsAdminAction
    data class UpdateCity(val city: String) : SigningsAdminAction
    data class UpdateContactNumber(val contactNumber: String) : SigningsAdminAction
    data class UpdateBirthday(val millis: Long): SigningsAdminAction
    data class UpdateEmail(val email: String) : SigningsAdminAction
    data class UpdatePesel(val pesel: String) : SigningsAdminAction
    data class UpdateCommunity(val community: String) : SigningsAdminAction
    data class UpdateType(val type: ParticipantType) : SigningsAdminAction
    data class UpdateWorkshop(val workshop: String) : SigningsAdminAction
    data class UpdateNotes(val notes: String) : SigningsAdminAction
    data object SaveData : SigningsAdminAction
    data object RemoveSigning : SigningsAdminAction
    data object HideNoInternetDialog : SigningsAdminAction
}