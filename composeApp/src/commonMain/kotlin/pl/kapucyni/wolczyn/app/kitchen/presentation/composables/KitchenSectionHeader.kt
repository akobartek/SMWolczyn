package pl.kapucyni.wolczyn.app.kitchen.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynTitleText
import pl.kapucyni.wolczyn.app.theme.wolczynColors

@Composable
fun KitchenSectionHeader(
    title: StringResource,
    icon: DrawableResource,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
    ) {
        Icon(
            imageVector = vectorResource(icon),
            contentDescription = null,
            tint = wolczynColors.accent,
            modifier = Modifier.width(34.dp)
        )
        WidthSpacer(12.dp)
        WolczynTitleText(
            text = stringResource(title),
            color = wolczynColors.accent
        )
    }
}