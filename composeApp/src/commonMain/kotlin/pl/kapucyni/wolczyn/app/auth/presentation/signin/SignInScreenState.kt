package pl.kapucyni.wolczyn.app.auth.presentation.signin

data class SignInScreenState(
    val loading: Boolean = false,
    val email: String = "",
    val emailError: EmailErrorType? = null,
    val password: String = "",
    val passwordHidden: Boolean = true,
    val passwordError: PasswordErrorType? = null,
    val isSignedIn: Boolean = false,
    val noInternetAction: NoInternetAction? = null,
    val forgottenPasswordDialogVisible: Boolean = false,
    val forgottenPasswordDialogError: Boolean = false,
    val emailUnverifiedDialogVisible: Boolean = false,
) {
    enum class EmailErrorType {
        INVALID,
        NO_USER,
    }

    enum class PasswordErrorType {
        EMPTY,
        INVALID,
        UNKNOWN,
    }

    enum class NoInternetAction {
        SIGN_IN,
        RESET_PASSWORD,
    }
}