package pl.kapucyni.wolczyn.app.archive.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveRecord
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText

@Composable
fun ArchiveRecordCard(record: ArchiveRecord) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(6.dp)
        ) {
            WolczynText(
                text = record.name,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16.sp,
                ),
                modifier = Modifier
                    .padding(6.dp)
                    .weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.Videocam,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                contentDescription = null
            )
        }
    }
}