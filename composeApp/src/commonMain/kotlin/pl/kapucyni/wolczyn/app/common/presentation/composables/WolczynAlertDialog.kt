package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WolczynAlertDialog(
    isVisible: Boolean,
    imageVector: ImageVector,
    dismissible: Boolean = true,
    dialogTitleId: StringResource,
    dialogTextId: StringResource,
    confirmBtnTextId: StringResource,
    onConfirm: () -> Unit,
    dismissBtnTextId: StringResource? = null,
    onDismissRequest: () -> Unit = {}
) {
    if (isVisible)
        AlertDialog(
            icon = {
                Icon(imageVector = imageVector, contentDescription = null)
            },
            title = {
                WolczynText(
                    text = stringResource(dialogTitleId),
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Center
                    ),
                )
            },
            text = {
                WolczynText(
                    text = stringResource(dialogTextId),
                    textStyle = MaterialTheme.typography.labelLarge
                )
            },
            onDismissRequest = { if (dismissible) onDismissRequest() },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    WolczynText(text = stringResource(confirmBtnTextId))
                }
            },
            dismissButton = {
                dismissBtnTextId?.let {
                    TextButton(onClick = onDismissRequest) {
                        WolczynText(text = stringResource(dismissBtnTextId))
                    }
                }
            }
        )
}