package pl.kapucyni.wolczyn.app.workshops

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.workshops.prayer.presentation.PrayerWorkshopDialog
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.workshops_prayer_name
import smwolczyn.composeapp.generated.resources.workshops_subtitle
import smwolczyn.composeapp.generated.resources.workshops_title

@Composable
fun WorkshopsScreen(onBackPressed: () -> Unit) {
    ScreenLayout(
        title = stringResource(Res.string.workshops_title),
        onBackPressed = onBackPressed
    ) {
        WorkshopsScreenContent()
    }
}

@Composable
fun WorkshopsScreenContent() {
    var prayerDialogVisible by rememberSaveable { mutableStateOf(false) }

    WolczynText(
        text = stringResource(Res.string.workshops_subtitle),
        textStyle = MaterialTheme.typography.titleLarge.copy(
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )

    Button(
        onClick = { prayerDialogVisible = true },
        modifier = Modifier.defaultMinSize(minWidth = 160.dp)
    ) {
        WolczynText(stringResource(Res.string.workshops_prayer_name))
    }

    PrayerWorkshopDialog(
        isVisible = prayerDialogVisible,
        onDismiss = { prayerDialogVisible = false },
    )
}