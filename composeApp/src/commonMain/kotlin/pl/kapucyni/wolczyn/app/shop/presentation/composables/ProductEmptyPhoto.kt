package pl.kapucyni.wolczyn.app.shop.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynLogo

@Composable
fun ProductEmptyPhoto(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        WolczynLogo()
    }
}