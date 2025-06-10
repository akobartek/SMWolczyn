package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ScreenHeader(
    title: String,
    onBackPressed: (() -> Unit)? = null,
    leadingIcon: (@Composable RowScope.() -> Unit)? = null,
    actionIcon: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        leadingIcon?.let { it() }
            ?: onBackPressed?.let { NavigateUpIcon(navigateUp = it) }
            ?: WidthSpacer(if (actionIcon == null) 0.dp else 40.dp)
        WolczynTitleText(
            text = title.replace("\n", " "),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        actionIcon?.let { it() } ?: WidthSpacer(if (onBackPressed == null) 0.dp else 40.dp)
    }
}