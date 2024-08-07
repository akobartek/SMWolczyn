package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.theme.wolczynColors

@Composable
fun PromotionBar(
    name: String,
    onRemove: ((String) -> Unit)?,
    modifier: Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = wolczynColors.alert
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .padding(vertical = if (onRemove != null) 6.dp else 0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.PriorityHigh,
                contentDescription = null,
                modifier = Modifier.padding(start = 12.dp)
            )
            WolczynText(
                text = name,
                textStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
            )
            onRemove?.let {
                IconButton(onClick = { onRemove(name) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        }
    }
}