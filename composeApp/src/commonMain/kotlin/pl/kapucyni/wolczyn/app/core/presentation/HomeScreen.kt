package pl.kapucyni.wolczyn.app.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.core.presentation.composables.HomeTileList
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynTitleText
import pl.kapucyni.wolczyn.app.theme.appColorPrimary
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.home_title

@Composable
fun HomeScreen(onTileClick: (HomeTileType) -> Unit) {
    var screenWidth by remember { mutableStateOf(0) }
    val screenWidthDp = with(LocalDensity.current) { screenWidth.toDp() }
    val columns = (screenWidthDp.value / 360).toInt()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .onGloballyPositioned { screenWidth = it.size.width }
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        WolczynTitleText(
            text = stringResource(Res.string.home_title),
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = appColorPrimary
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            HomeTileList(
                columns = columns,
                onTileClick = onTileClick
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}