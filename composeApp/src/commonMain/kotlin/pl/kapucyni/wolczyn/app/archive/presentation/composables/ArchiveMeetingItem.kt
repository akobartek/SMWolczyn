package pl.kapucyni.wolczyn.app.archive.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText

@Composable
fun ArchiveMeetingItem(meeting: ArchiveMeeting) {
    Card(
        shape = RoundedCornerShape(12.dp),
        onClick = {/*TODO*/ },
    ) {
        Box(modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
        ) {
            AsyncImage(
                model = meeting.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            WolczynText(
                text = meeting.name,
                textStyle = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                ),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(color = Color(0xCC000000))
                    .padding(6.dp)
            )
        }
    }
}