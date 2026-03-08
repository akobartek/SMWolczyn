package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.vectorResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.ic_no_internet
import smwolczyn.composeapp.generated.resources.no_internet_reconnect_message
import smwolczyn.composeapp.generated.resources.no_internet_title
import smwolczyn.composeapp.generated.resources.try_again

@Composable
fun NoInternetDialog(
    isVisible: Boolean,
    onReconnect: () -> Unit,
    onDismiss: () -> Unit
) {
    WolczynAlertDialog(
        isVisible = isVisible,
        imageVector = vectorResource(Res.drawable.ic_no_internet),
        dialogTitleId = Res.string.no_internet_title,
        dialogTextId = Res.string.no_internet_reconnect_message,
        confirmBtnTextId = Res.string.try_again,
        onConfirm = onReconnect,
        dismissBtnTextId = Res.string.cancel,
        dismissible = false,
        onDismissRequest = onDismiss
    )
}