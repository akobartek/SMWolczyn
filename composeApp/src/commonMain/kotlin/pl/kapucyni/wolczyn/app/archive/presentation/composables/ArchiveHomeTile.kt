package pl.kapucyni.wolczyn.app.archive.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.common.presentation.composables.HomeTile
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.archive_title

@Composable
fun ArchiveHomeTile(
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeTile(
        nameRes = Res.string.archive_title,
        nameAlignment = Alignment.TopEnd,
        height = 170.dp,
        backgroundColor = backgroundColor,
        image = {
            // TODO
        },
        onClick = onClick,
        modifier = modifier
    )
}