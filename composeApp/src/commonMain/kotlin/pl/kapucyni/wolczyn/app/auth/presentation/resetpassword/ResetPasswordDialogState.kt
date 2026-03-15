package pl.kapucyni.wolczyn.app.auth.presentation.resetpassword

import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpPasswordError

data class ResetPasswordDialogState(
    val resetCode: String,
    val email: String,
    val loading: Boolean = false,
    val passwordError: SignUpPasswordError? = null,
    val resetFailed: Boolean = false,
)
