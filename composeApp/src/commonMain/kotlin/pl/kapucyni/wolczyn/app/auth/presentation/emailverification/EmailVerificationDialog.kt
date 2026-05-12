package pl.kapucyni.wolczyn.app.auth.presentation.emailverification

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.vectorResource
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.CloseVerificationDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.theme.AppTheme
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_manage_accounts
import smwolczyn.composeapp.generated.resources.ok
import smwolczyn.composeapp.generated.resources.verify_email_confirmed_message
import smwolczyn.composeapp.generated.resources.verify_email_dialog_title
import smwolczyn.composeapp.generated.resources.verify_email_error_message

@Composable
fun EmailVerificationDialog(
    state: EmailVerificationDialogState?,
    handleAction: (AuthAction) -> Unit,
) {
    WolczynAlertDialog(
        isVisible = state != null,
        imageVector = vectorResource(Res.drawable.ic_manage_accounts),
        dialogTitleId = Res.string.verify_email_dialog_title,
        dialogTextId =
            if (state?.success == true) Res.string.verify_email_confirmed_message
            else Res.string.verify_email_error_message,
        confirmBtnTextId = Res.string.ok,
        onConfirm = { handleAction(CloseVerificationDialog) },
        dismissible = false,
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        EmailVerificationDialog(
            state = EmailVerificationDialogState(
                verificationCode = "123456",
                success = true,
            ),
            handleAction = {},
        )
    }
}