package pl.kapucyni.wolczyn.app.archive.presentation.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.runtime.Composable
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.no_records_dialog_message
import smwolczyn.composeapp.generated.resources.no_records_dialog_title
import smwolczyn.composeapp.generated.resources.ok

@Composable
fun ArchiveNoRecordsDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
) {
    WolczynAlertDialog(
        isVisible = isVisible,
        imageVector = Icons.Default.VideocamOff,
        dialogTitleId = Res.string.no_records_dialog_title,
        dialogTextId = Res.string.no_records_dialog_message,
        confirmBtnTextId = Res.string.ok,
        onConfirm = onDismiss,
        onDismissRequest = onDismiss
    )
}