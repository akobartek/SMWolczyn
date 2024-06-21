package pl.kapucyni.wolczyn.app.kitchen.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynTitleText
import pl.kapucyni.wolczyn.app.theme.appColorTertiary

@Composable
fun KitchenSectionHeader(
    title: String,
    icon: Painter,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = appColorTertiary,
            modifier = Modifier.width(34.dp)
        )
        WidthSpacer(12.dp)
        WolczynTitleText(
            text = title,
            color = appColorTertiary
        )
    }
}