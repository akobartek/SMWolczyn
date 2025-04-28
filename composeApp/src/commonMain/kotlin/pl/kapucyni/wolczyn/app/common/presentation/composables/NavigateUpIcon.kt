package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.navigateUpIcon
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_navigate_up

@Composable
fun NavigateUpIcon(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
) {
    IconButton(onClick = navigateUp, modifier = modifier) {
        Icon(
            imageVector = navigateUpIcon,
            tint = wolczynColors.primary,
            contentDescription = stringResource(Res.string.cd_navigate_up)
        )
    }
}