package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType.MEMBER
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.*
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import pl.kapucyni.wolczyn.app.core.domain.model.AppVersion
import pl.kapucyni.wolczyn.app.core.presentation.getHomeTile
import pl.kapucyni.wolczyn.app.theme.wolczynColors

@Composable
fun HomeTileList(
    columns: Int,
    appConfiguration: AppConfiguration,
    user: User?,
    onTileClick: (HomeTileType) -> Unit,
) {
    val meetingTile = when {
        user == null || user.userType == MEMBER ->
            appConfiguration.openSigning?.let { SIGNING }

        else -> MEETINGS
    }
    val tiles = when (columns) {
        1 -> oneColumn(appConfiguration.appVersion, meetingTile)
        2 -> twoColumns(appConfiguration.appVersion, meetingTile)
        else -> threeColumns(appConfiguration.appVersion, meetingTile)
    }

    tiles.forEachIndexed { i, row ->
        if (row.size == 1) {
            val tileType = row[0]
            tileType?.let {
                getHomeTile(
                    tileType = tileType,
                    backgroundColor = getTileBackground(i),
                    onClick = { onTileClick(tileType) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                row.forEachIndexed { j, tileType ->
                    tileType?.let {
                        getHomeTile(
                            tileType = tileType,
                            backgroundColor = getTileBackground(i + j),
                            onClick = { onTileClick(tileType) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
    }
}

private fun getTileBackground(num: Int) =
    if (num % 2 == 0) wolczynColors.accent else wolczynColors.secondary

private fun oneColumn(appVersion: AppVersion, meetingTile: HomeTileType?) = when (appVersion) {
    AppVersion.MEETING -> {
        arrayOf(
            arrayOf(meetingTile),
            arrayOf(SCHEDULE),
            arrayOf(SONG_BOOK),
            arrayOf(KITCHEN),
            arrayOf(SHOP),
            arrayOf(BREVIARY),
            arrayOf(WEATHER),
            arrayOf(DECALOGUE),
            arrayOf(ARCHIVE),
        )
    }

    AppVersion.NO_MEETING -> {
        arrayOf(
            arrayOf(meetingTile),
            arrayOf(SONG_BOOK),
            arrayOf(BREVIARY),
            arrayOf(ARCHIVE),
        )
    }
}

private fun twoColumns(appVersion: AppVersion, meetingTile: HomeTileType?) = when (appVersion) {
    AppVersion.MEETING -> {
        arrayOf(
            arrayOf(meetingTile),
            arrayOf(SCHEDULE, SONG_BOOK),
            arrayOf(KITCHEN, SHOP),
            arrayOf(BREVIARY, WEATHER),
            arrayOf(DECALOGUE, ARCHIVE),
        )
    }

    AppVersion.NO_MEETING -> {
        arrayOf(
            arrayOf(meetingTile, SONG_BOOK),
            arrayOf(BREVIARY, ARCHIVE),
        )
    }
}

private fun threeColumns(appVersion: AppVersion, meetingTile: HomeTileType?) = when (appVersion) {
    AppVersion.MEETING -> {
        arrayOf(
            arrayOf(SCHEDULE, meetingTile, SONG_BOOK),
            arrayOf(SHOP, BREVIARY, KITCHEN),
            arrayOf(WEATHER, ARCHIVE, DECALOGUE),
        )
    }

    AppVersion.NO_MEETING -> {
        meetingTile?.let {
            arrayOf(
                arrayOf(meetingTile, SONG_BOOK),
                arrayOf(BREVIARY, ARCHIVE),
            )
        } ?: arrayOf(arrayOf(SONG_BOOK, BREVIARY, ARCHIVE))
    }
}