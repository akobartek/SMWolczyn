package pl.kapucyni.wolczyn.app.common.presentation.snackbars

import org.jetbrains.compose.resources.StringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.delete_account_success
import smwolczyn.composeapp.generated.resources.message_sent
import smwolczyn.composeapp.generated.resources.sign_in_error
import smwolczyn.composeapp.generated.resources.sign_up_error
import smwolczyn.composeapp.generated.resources.signed_in

sealed class SnackbarEvent(
    val message: StringResource,
    val action: SnackbarAction? = null,
) {
    data object SignedIn : SnackbarEvent(message = Res.string.signed_in)

    data object SignInError : SnackbarEvent(message = Res.string.sign_in_error)

    data object SignUpError : SnackbarEvent(message = Res.string.sign_up_error)

    data object ResetPasswordMessageSent : SnackbarEvent(message = Res.string.message_sent)

    data object AccountDeleted : SnackbarEvent(message = Res.string.delete_account_success)
}