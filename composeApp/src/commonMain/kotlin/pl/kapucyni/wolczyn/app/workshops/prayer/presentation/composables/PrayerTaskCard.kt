package pl.kapucyni.wolczyn.app.workshops.prayer.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.model.PrayerWorkshopTask

@Composable
fun PrayerTaskCard(task: PrayerWorkshopTask) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        onClick = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            WolczynText(
                text = task.name,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            )

            PrayerTaskImage(
                photoUrl = task.photoUrl,
                isExpanded = expanded,
            )
        }
    }
}