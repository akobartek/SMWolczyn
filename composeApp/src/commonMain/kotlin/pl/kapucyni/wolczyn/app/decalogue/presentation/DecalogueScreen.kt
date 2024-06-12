package pl.kapucyni.wolczyn.app.decalogue.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.decalogue.presentation.composables.DecalogueElement
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.decalogue_rules
import smwolczyn.composeapp.generated.resources.decalogue_title

@Composable
fun DecalogueScreen(onBackPressed: () -> Unit) {
    ScreenLayout(
        title = stringResource(Res.string.decalogue_title),
        onBackPressed = onBackPressed
    ) {
        DecalogueScreenContent()
    }
}

@Composable
fun DecalogueScreenContent() {
    val decalogue = arrayOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X")
        .zip(stringArrayResource(Res.array.decalogue_rules))

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 20.dp)
    ) {
        items(items = decalogue, key = { it.first }) { pair ->
            DecalogueElement(pair)
        }
    }
}