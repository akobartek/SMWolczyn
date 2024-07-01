package pl.kapucyni.wolczyn.app.kitchen.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.HomeTile
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_cap_kitchen
import smwolczyn.composeapp.generated.resources.kitchen_title

@Composable
fun KitchenHomeTile(
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeTile(
        nameRes = Res.string.kitchen_title,
        nameAlignment = Alignment.TopStart,
        height = 180.dp,
        backgroundColor = backgroundColor,
        image = {
            Image(
                painter = painterResource(Res.drawable.ic_cap_kitchen),
                contentScale = FixedScale(0.39f),
                contentDescription = null,
                modifier = Modifier.align(Alignment.BottomEnd)
                    .offset(x = 10.dp, y = 25.dp)
            )
        },
        onClick = onClick,
        modifier = modifier
    )
}