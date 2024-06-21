package pl.kapucyni.wolczyn.app.archive.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting

@Composable
fun ArchiveMeetingItem(meeting: ArchiveMeeting) {
    Card(
        shape = RoundedCornerShape(12.dp),
        onClick = {/*TODO*/ },
    ) {
        Box(modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()) {
            AsyncImage(
                model = meeting.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}