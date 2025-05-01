package pl.kapucyni.wolczyn.app.meetings.presentation.signings

import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

sealed interface SigningsAction {
    data class UpdateFirstName(val firstName: String) : SigningsAction
    data class UpdateLastName(val lastName: String) : SigningsAction
    data class UpdateCity(val city: String) : SigningsAction
    data class UpdateBirthday(val millis: Long): SigningsAction
    data class UpdateEmail(val email: String) : SigningsAction
    data class UpdatePesel(val pesel: String) : SigningsAction
    data class UpdateType(val type: ParticipantType) : SigningsAction
    data class UpdateWorkshop(val workshop: String) : SigningsAction
    data class UpdateStatuteConsent(val checked: Boolean) : SigningsAction
    data object SaveData : SigningsAction
    data object RemoveSigning : SigningsAction
    data object HideSuccessDialog : SigningsAction
    data object HideNoInternetDialog : SigningsAction
}