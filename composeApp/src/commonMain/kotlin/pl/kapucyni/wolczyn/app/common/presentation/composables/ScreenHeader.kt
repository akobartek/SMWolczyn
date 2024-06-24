package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.theme.appColorPrimary
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_navigate_up

@Composable
fun ScreenHeader(
    title: String,
    onBackPressed: (() -> Unit)? = null,
    actionIcon: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        onBackPressed?.let {
            IconButton(onClick = it) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    tint = appColorPrimary,
                    contentDescription = stringResource(Res.string.cd_navigate_up)
                )
            }
        }
        WolczynTitleText(
            text = title.replace("\n", " "),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        actionIcon?.let { it() } ?: WidthSpacer(if (onBackPressed == null) 0.dp else 40.dp)
    }
}