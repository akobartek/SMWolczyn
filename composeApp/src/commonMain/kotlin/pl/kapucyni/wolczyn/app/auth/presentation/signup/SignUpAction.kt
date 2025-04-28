package pl.kapucyni.wolczyn.app.auth.presentation.signup

sealed interface SignUpAction {
    data class UpdateEmail(val email: String) : SignUpAction
    data class UpdatePassword(val password: String) : SignUpAction
    data object TogglePasswordHidden : SignUpAction
    data class UpdateFirstName(val firstName: String) : SignUpAction
    data class UpdateLastName(val lastName: String) : SignUpAction
    data class UpdateCity(val city: String) : SignUpAction
    data class UpdateBirthday(val millis: Long): SignUpAction
    data object SignUp : SignUpAction
    data object ToggleSignUpSuccessDialog : SignUpAction
    data object ToggleAccountExistsDialog : SignUpAction
    data object ToggleNoInternetDialog : SignUpAction
}