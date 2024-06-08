package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.ARCHIVE
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.BREVIARY
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.DECALOGUE
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.KITCHEN
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.SCHEDULE
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.SHOP
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.SONG_BOOK
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.WEATHER
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynTitleText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.archive_title
import smwolczyn.composeapp.generated.resources.breviary_title
import smwolczyn.composeapp.generated.resources.decalogue_title
import smwolczyn.composeapp.generated.resources.ic_cap_decalogue
import smwolczyn.composeapp.generated.resources.ic_cap_kitchen
import smwolczyn.composeapp.generated.resources.ic_cap_song_book
import smwolczyn.composeapp.generated.resources.ic_cap_weather
import smwolczyn.composeapp.generated.resources.kitchen_title
import smwolczyn.composeapp.generated.resources.schedule_title
import smwolczyn.composeapp.generated.resources.shop_title
import smwolczyn.composeapp.generated.resources.song_book_title
import smwolczyn.composeapp.generated.resources.weather_title

@Composable
fun getHomeTile(
    tileType: HomeTileType,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (tileType) {
        SCHEDULE -> ScheduleTile(backgroundColor, onClick, modifier)
        SONG_BOOK -> SongBookTile(backgroundColor, onClick, modifier)
        KITCHEN -> KitchenTile(backgroundColor, onClick, modifier)
        SHOP -> ShopTile(backgroundColor, onClick, modifier)
        DECALOGUE -> DecalogueTile(backgroundColor, onClick, modifier)
        WEATHER -> WeatherTile(backgroundColor, modifier)
        BREVIARY -> BreviaryTile(backgroundColor, onClick, modifier)
        ARCHIVE -> ArchiveTile(backgroundColor, onClick, modifier)
    }
}

@Composable
private fun HomeTile(
    nameRes: StringResource,
    nameAlignment: Alignment,
    height: Dp,
    backgroundColor: Color,
    image: @Composable BoxScope.() -> Unit,
    additionalContent: @Composable ColumnScope.() -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors().copy(containerColor = backgroundColor),
        modifier = modifier.height(height)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(nameAlignment)
            ) {
                WolczynTitleText(
                    text = stringResource(nameRes),
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(20.dp)
                )
                additionalContent()
            }

            image()
        }

    }
}

@Composable
private fun ScheduleTile(
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeTile(
        nameRes = Res.string.schedule_title,
        nameAlignment = Alignment.TopStart,
        height = 170.dp,
        backgroundColor = backgroundColor,
        image = {
            // TODO
        },
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun SongBookTile(
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

@Composable
private fun KitchenTile(
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeTile(
        nameRes = Res.string.kitchen_title,
        nameAlignment = Alignment.TopStart,
        height = 240.dp,
        backgroundColor = backgroundColor,
        image = {
            Image(
                painter = painterResource(Res.drawable.ic_cap_kitchen),
                contentScale = FixedScale(0.42f),
                contentDescription = null,
                modifier = Modifier.align(Alignment.BottomEnd)
                    .offset(x = 25.dp, y = 25.dp)
            )
        },
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun ShopTile(
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeTile(
        nameRes = Res.string.shop_title,
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

@Composable
private fun DecalogueTile(
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeTile(
        nameRes = Res.string.decalogue_title,
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
                    .padding(end = 16.dp, top = 8.dp)
                    .scale(scaleX = -1f, scaleY = 1f)
            )
        },
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun WeatherTile(
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    HomeTile(
        nameRes = Res.string.weather_title,
        nameAlignment = Alignment.TopEnd,
        height = 170.dp,
        backgroundColor = backgroundColor,
        image = {
            Image(
                painter = painterResource(Res.drawable.ic_cap_weather),
                contentScale = FixedScale(0.5f),
                alignment = BiasAlignment(0f, -0.8f),
                contentDescription = null,
                modifier = Modifier.padding(start = 10.dp)
            )
        },
        onClick = { },
        modifier = modifier
    )
}

@Composable
private fun BreviaryTile(
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeTile(
        nameRes = Res.string.breviary_title,
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

@Composable
private fun ArchiveTile(
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