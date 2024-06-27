package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.*
import pl.kapucyni.wolczyn.app.core.domain.model.AppVersion
import pl.kapucyni.wolczyn.app.core.presentation.getHomeTile
import pl.kapucyni.wolczyn.app.theme.appColorSecondary
import pl.kapucyni.wolczyn.app.theme.appColorTertiary

@Composable
fun HomeTileList(
    columns: Int,
    appVersion: AppVersion,
    onTileClick: (HomeTileType) -> Unit,
) {
    val tiles = when (columns) {
        1 -> oneColumn(appVersion)
        2 -> twoColumns(appVersion)
        else -> threeColumns(appVersion)
    }

    tiles.forEachIndexed { i, row ->
        if (row.size == 1) {
            val tileType = row[0]
            getHomeTile(
                tileType = tileType,
                backgroundColor = getTileBackground(i),
                onClick = { onTileClick(tileType) },
                modifier = Modifier.fillMaxWidth()
            )
        } else
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                row.forEachIndexed { j, tileType ->
                    getHomeTile(
                        tileType = tileType,
                        backgroundColor = getTileBackground(i + j),
                        onClick = { onTileClick(tileType) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
    }
}

private fun getTileBackground(num: Int) =
    if (num % 2 == 0) appColorTertiary else appColorSecondary

private fun oneColumn(appVersion: AppVersion) = when (appVersion) {
    AppVersion.MEETING -> {
        arrayOf(
            arrayOf(SCHEDULE),
            arrayOf(SONG_BOOK),
            arrayOf(KITCHEN),
            arrayOf(SHOP),
            arrayOf(WEATHER),
            arrayOf(DECALOGUE),
            arrayOf(BREVIARY),
            arrayOf(ARCHIVE),
        )
    }

    AppVersion.NO_MEETING -> {
        arrayOf(
            arrayOf(SONG_BOOK),
            arrayOf(BREVIARY),
            arrayOf(ARCHIVE),
        )
    }
}

private fun twoColumns(appVersion: AppVersion) = when (appVersion) {
    AppVersion.MEETING -> {
        arrayOf(
            arrayOf(SCHEDULE, SONG_BOOK),
            arrayOf(KITCHEN, SHOP),
            arrayOf(BREVIARY, WEATHER),
            arrayOf(DECALOGUE, ARCHIVE),
        )
    }

    AppVersion.NO_MEETING -> {
        arrayOf(
            arrayOf(SONG_BOOK, ARCHIVE),
            arrayOf(BREVIARY)
        )
    }
}

private fun threeColumns(appVersion: AppVersion) = when (appVersion) {
    AppVersion.MEETING -> {
        arrayOf(
            arrayOf(SCHEDULE, SONG_BOOK, BREVIARY),
            arrayOf(SHOP, KITCHEN, WEATHER),
            arrayOf(DECALOGUE, ARCHIVE),
        )
    }

    AppVersion.NO_MEETING -> {
        arrayOf(
            arrayOf(SONG_BOOK, BREVIARY, ARCHIVE),
        )
    }
}