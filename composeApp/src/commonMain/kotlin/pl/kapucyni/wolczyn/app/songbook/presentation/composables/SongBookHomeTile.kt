package pl.kapucyni.wolczyn.app.songbook.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.HomeTile
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_cap_song_book
import smwolczyn.composeapp.generated.resources.song_book_title

@Composable
fun SongBookHomeTile(
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeTile(
        nameRes = Res.string.song_book_title,
        nameAlignment = Alignment.TopEnd,
        height = 170.dp,
        backgroundColor = backgroundColor,
        image = {
            Image(
                painter = painterResource(Res.drawable.ic_cap_song_book),
                contentScale = FixedScale(0.57f),
                alignment = BiasAlignment(0f, -1f),
                contentDescription = null,
                modifier = Modifier.padding(start = 20.dp, top = 8.dp)
            )
        },
        onClick = onClick,
        modifier = modifier
    )
}