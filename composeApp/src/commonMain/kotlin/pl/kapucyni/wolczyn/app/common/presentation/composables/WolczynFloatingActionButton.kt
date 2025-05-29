package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.fab.FloatingButtonData

@Composable
fun WolczynFloatingActionButton(data: FloatingButtonData) {
    if (data.enabled.not()) return

    if (data.isSmall) {
        SmallFloatingActionButton(onClick = data.onClick) {
            Icon(
                imageVector = data.icon,
                contentDescription = stringResource(data.contentDescription),
            )
        }
    } else {
        FloatingActionButton(onClick = data.onClick) {
            Icon(
                imageVector = data.icon,
                contentDescription = stringResource(data.contentDescription),
            )
        }
    }
}