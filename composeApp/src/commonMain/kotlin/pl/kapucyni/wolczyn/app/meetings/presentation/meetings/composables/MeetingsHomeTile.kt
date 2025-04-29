package pl.kapucyni.wolczyn.app.meetings.presentation.meetings.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.HomeTile
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_cap_decalogue
import smwolczyn.composeapp.generated.resources.meetings_title

@Composable
fun MeetingsHomeTile(
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeTile(
        nameRes = Res.string.meetings_title,
        nameAlignment = Alignment.TopStart,
        height = 170.dp,
        backgroundColor = backgroundColor,
        image = {
            Image(
                painter = painterResource(Res.drawable.ic_cap_decalogue),
                contentScale = FixedScale(0.5f),
                alignment = BiasAlignment(0f, -1f),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd)
                    .padding(end = 14.dp, top = 8.dp)
                    .scale(scaleX = -1f, scaleY = 1f)
            )
        },
        onClick = onClick,
        modifier = modifier,
    )
}