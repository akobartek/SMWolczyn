package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.core.presentation.HomeTileType.*
import pl.kapucyni.wolczyn.app.theme.appColorSecondary
import pl.kapucyni.wolczyn.app.theme.appColorTertiary

@Composable
fun HomeTileList(columns: Int) {
    val tiles = when (columns) {
        1 -> oneColumn
        2 -> twoColumns
        else -> threeColumns
    }

    tiles.forEachIndexed { i, row ->
        if (row.size == 1)
            getHomeTile(
                tileType = row[0],
                backgroundColor = getTileBackground(i),
                modifier = Modifier.fillMaxWidth()
            )
        else
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEachIndexed { j, tileType ->
                    getHomeTile(
                        tileType = tileType,
                        backgroundColor = getTileBackground(i + j),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
    }
}

private fun getTileBackground(num: Int) = if (num % 2 == 0) appColorSecondary else appColorTertiary

private val oneColumn = arrayOf(
    arrayOf(AGENDA),
    arrayOf(SONG_BOOK),
    arrayOf(KITCHEN),
    arrayOf(SHOP),
    arrayOf(DECALOGUE),
    arrayOf(WEATHER),
    arrayOf(BREVIARY),
    arrayOf(ARCHIVE)
)

private val twoColumns = arrayOf(
    arrayOf(AGENDA, SONG_BOOK),
    arrayOf(KITCHEN, SHOP),
    arrayOf(DECALOGUE, WEATHER),
    arrayOf(BREVIARY, ARCHIVE)
)

private val threeColumns = arrayOf(
    arrayOf(AGENDA, SONG_BOOK, KITCHEN),
    arrayOf(SHOP, DECALOGUE, WEATHER),
    arrayOf(BREVIARY, ARCHIVE)
)