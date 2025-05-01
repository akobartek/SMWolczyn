package pl.kapucyni.wolczyn.app.common.presentation.snackbars

import org.jetbrains.compose.resources.StringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.data_save_error
import smwolczyn.composeapp.generated.resources.delete_account_failure
import smwolczyn.composeapp.generated.resources.delete_account_success
import smwolczyn.composeapp.generated.resources.edit_profile_success
import smwolczyn.composeapp.generated.resources.meeting_signing_remove_success
import smwolczyn.composeapp.generated.resources.meeting_signing_saved
import smwolczyn.composeapp.generated.resources.meeting_signing_updated
import smwolczyn.composeapp.generated.resources.message_sent
import smwolczyn.composeapp.generated.resources.sign_in_error
import smwolczyn.composeapp.generated.resources.sign_up_error
import smwolczyn.composeapp.generated.resources.signed_in
import smwolczyn.composeapp.generated.resources.signed_out
import smwolczyn.composeapp.generated.resources.verify_email_email_sent

sealed class SnackbarEvent(
    val message: StringResource,
    val action: SnackbarAction? = null,
) {
    data object DataSaveError : SnackbarEvent(message = Res.string.data_save_error)

    data object SignedIn : SnackbarEvent(message = Res.string.signed_in)

    data object SignInError : SnackbarEvent(message = Res.string.sign_in_error)

    data object SignUpError : SnackbarEvent(message = Res.string.sign_up_error)

    data object SignedOut : SnackbarEvent(message = Res.string.signed_out)

    data object EditProfileSuccess : SnackbarEvent(message = Res.string.edit_profile_success)

    data object ResetPasswordMessageSent : SnackbarEvent(message = Res.string.message_sent)

    data object VerifyEmailMessageSent : SnackbarEvent(message = Res.string.verify_email_email_sent)

    data object AccountDeleted : SnackbarEvent(message = Res.string.delete_account_success)

    data object AccountDeleteFailed : SnackbarEvent(message = Res.string.delete_account_failure)

    data object MeetingSigningSaved : SnackbarEvent(message = Res.string.meeting_signing_saved)

    data object MeetingSigningUpdated : SnackbarEvent(message = Res.string.meeting_signing_updated)

    data object MeetingSigningRemoved : SnackbarEvent(message = Res.string.meeting_signing_remove_success)
}