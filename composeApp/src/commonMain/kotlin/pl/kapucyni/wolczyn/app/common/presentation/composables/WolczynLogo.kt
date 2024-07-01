package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.wolczyn_logo

@Composable
fun WolczynLogo(
    size: Dp = 80.dp,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(Res.drawable.wolczyn_logo),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier.width(size),
    )
}