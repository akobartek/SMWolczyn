package pl.kapucyni.wolczyn.app.workshops.prayer.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.wolczyn_logo

@Composable
fun PrayerTaskImage(
    photoUrl: String,
    isExpanded: Boolean,
) {
    AnimatedVisibility(
        visible = isExpanded,
        modifier = Modifier.fillMaxSize(),
    ) {
        AsyncImage(
            model = photoUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            placeholder = painterResource(Res.drawable.wolczyn_logo),
            modifier = Modifier.fillMaxSize(),
        )
    }
}