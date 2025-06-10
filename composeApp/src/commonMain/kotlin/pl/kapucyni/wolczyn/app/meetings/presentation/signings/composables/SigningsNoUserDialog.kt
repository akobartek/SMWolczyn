package pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.runtime.Composable
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.sign_in
import smwolczyn.composeapp.generated.resources.signings_no_user_dialog_message
import smwolczyn.composeapp.generated.resources.signings_no_user_dialog_title

@Composable
fun SigningsNoUserDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    WolczynAlertDialog(
        isVisible = true,
        imageVector = Icons.Default.ManageAccounts,
        dismissible = false,
        dialogTitleId = Res.string.signings_no_user_dialog_title,
        dialogTextId = Res.string.signings_no_user_dialog_message,
        confirmBtnTextId = Res.string.sign_in,
        onConfirm = onConfirm,
        dismissBtnTextId = Res.string.cancel,
        onDismissRequest = onCancel,
    )
}