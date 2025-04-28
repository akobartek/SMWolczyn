package pl.kapucyni.wolczyn.app.auth.presentation.signup

data class SignUpScreenState(
    val loading: Boolean = false,
    val firstName: String = "",
    val firstNameError: Boolean = false,
    val lastName: String = "",
    val lastNameError: Boolean = false,
    val city: String = "",
    val cityError: Boolean = false,
    val email: String = "",
    val emailError: Boolean = false,
    val password: String = "",
    val passwordHidden: Boolean = true,
    val passwordError: PasswordErrorType? = null,
    val birthdayDate: Long? = null,
    val birthdayError: Boolean = false,
    val consentsChecked: Boolean = false,
    val isSignedUpDialogVisible: Boolean = false,
    val accountExistsDialogVisible: Boolean = false,
    val noInternetDialogVisible: Boolean = false,
) {
    enum class PasswordErrorType {
        TOO_SHORT,
        WRONG,
    }
}
