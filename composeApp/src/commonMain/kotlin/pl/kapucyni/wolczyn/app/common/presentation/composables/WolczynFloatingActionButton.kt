package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.theme.wolczynColors

@Composable
fun WolczynFloatingActionButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: StringResource,
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = wolczynColors.primary,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(contentDescription),
        )
    }
}