package pl.kapucyni.wolczyn.app.auth.presentation.emailverification

data class EmailVerificationDialogState(
    val verificationCode: String,
    val success: Boolean,
)
