package pl.kapucyni.wolczyn.app.auth.presentation

sealed interface AuthAction {
    data object SignOut : AuthAction
    data object ResetPassword : AuthAction
    data object DeleteAccount : AuthAction
    data object CloseResetDialog: AuthAction
    data class SetNewPassword(val password: String) : AuthAction
}