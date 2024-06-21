package pl.kapucyni.wolczyn.app.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.core.presentation.composables.HomeTileList
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.home_title

@Composable
fun HomeScreen(onTileClick: (HomeTileType) -> Unit) {
    var screenWidth by remember { mutableStateOf(0) }
    val screenWidthDp = with(LocalDensity.current) { screenWidth.toDp() }
    val columns = (screenWidthDp.value / 360).toInt().coerceIn(1, 3)

    ScreenLayout(
        title = stringResource(Res.string.home_title),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .onGloballyPositioned { screenWidth = it.size.width }
                .padding(horizontal = 20.dp)
        ) {
            HomeTileList(
                columns = columns,
                onTileClick = onTileClick,
            )
        }
    }
}