package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_close_dialog
import smwolczyn.composeapp.generated.resources.save

@Composable
fun FullScreenDialog(
    isVisible: Boolean,
    title: String,
    actionTitle: StringResource? = null,
    onAction: (() -> Unit)? = {},
    onDismiss: () -> Unit,
    action: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
) {
    if (isVisible)
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            val focusManager = LocalFocusManager.current
            val interactionSource = remember { MutableInteractionSource() }

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = wolczynColors.primary,
                            contentDescription = stringResource(Res.string.cd_close_dialog),
                        )
                    }
                    WolczynText(
                        text = title,
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            textAlign = TextAlign.Center,
                            color = wolczynColors.primary,
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                    onAction?.let {
                        TextButton(onClick = onAction) {
                            WolczynText(text = stringResource(actionTitle ?: Res.string.save))
                        }
                    } ?: WidthSpacer(40.dp)
                    action()
                }
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                        .fillMaxSize()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { focusManager.clearFocus(true) }
                ) {
                    content()
                }
            }
        }
}