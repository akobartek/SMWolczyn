package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.storeLink
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.force_update_dialog_btn
import smwolczyn.composeapp.generated.resources.force_update_dialog_message
import smwolczyn.composeapp.generated.resources.force_update_dialog_title

@Composable
fun ForceUpdateDialog(isVisible: Boolean) {
    val uriHandler = LocalUriHandler.current

    WolczynAlertDialog(
        isVisible = isVisible,
        imageVector = Icons.Default.NewReleases,
        dialogTitleId = Res.string.force_update_dialog_title,
        dialogTextId = Res.string.force_update_dialog_message,
        confirmBtnTextId = Res.string.force_update_dialog_btn,
        onConfirm = { uriHandler.openUri(storeLink) },
        onDismissRequest = {},
        dismissible = false,
    )
}