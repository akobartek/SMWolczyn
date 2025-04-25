package pl.kapucyni.wolczyn.app.auth.presentation.signup

data class SignUpScreenState(
    val loading: Boolean = false,
    val email: String = "",
    val emailError: Boolean = false,
    val password: String = "",
    val passwordHidden: Boolean = true,
    val passwordError: PasswordErrorType? = null,
    val birthdayDate: Long? = null,
    val isSignedUpDialogVisible: Boolean = false,
    val accountExistsDialogVisible: Boolean = false,
    val noInternetDialogVisible: Boolean = false,
) {
    enum class PasswordErrorType {
        TOO_SHORT,
        WRONG,
    }
}
