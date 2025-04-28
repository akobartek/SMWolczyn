package pl.kapucyni.wolczyn.app.auth.presentation

sealed interface AuthAction {
    data object SignOut : AuthAction
    data object ResetPassword : AuthAction
    data object DeleteAccount : AuthAction
}