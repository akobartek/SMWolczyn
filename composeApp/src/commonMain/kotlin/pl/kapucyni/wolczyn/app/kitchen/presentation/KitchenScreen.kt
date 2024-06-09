package pl.kapucyni.wolczyn.app.kitchen.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynTitleText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.kitchen_title
import smwolczyn.composeapp.generated.resources.menu

@Composable
fun KitchenScreen(onBackPressed: () -> Unit) {
    ScreenLayout(
        title = stringResource(Res.string.kitchen_title),
        onBackPressed = onBackPressed
    ) {
        KitchenScreenContent()
    }
}

@Composable
fun KitchenScreenContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        item {
            WolczynTitleText(
                text = stringResource(Res.string.menu),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}