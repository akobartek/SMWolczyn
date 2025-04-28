package pl.kapucyni.wolczyn.app.auth.presentation.edit

sealed interface EditProfileAction {
    data class UpdateFirstName(val firstName: String) : EditProfileAction
    data class UpdateLastName(val lastName: String) : EditProfileAction
    data class UpdateCity(val city: String) : EditProfileAction
    data class UpdateBirthday(val millis: Long) : EditProfileAction
    data object SaveData : EditProfileAction
    data object ToggleNoInternetDialog : EditProfileAction
}