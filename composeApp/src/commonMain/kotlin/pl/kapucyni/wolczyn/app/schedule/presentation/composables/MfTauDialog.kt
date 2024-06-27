package pl.kapucyni.wolczyn.app.schedule.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.painterResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.mftau

@Composable
fun MfTauDialog(
    isVisible: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (isVisible)
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Image(
                painter = painterResource(Res.drawable.mftau),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
            )
        }
}