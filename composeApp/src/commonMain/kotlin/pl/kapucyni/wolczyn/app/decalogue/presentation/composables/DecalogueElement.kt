package pl.kapucyni.wolczyn.app.decalogue.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText

@Composable
fun DecalogueElement(pair: Pair<String, String>) {
    val (number, rule) = pair

    Row {
        WolczynText(
            text = "$number.",
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End
            ),
            modifier = Modifier.width(48.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        WolczynText(
            text = rule,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Justify
            ),
            modifier = Modifier.weight(1f)
        )
    }
}