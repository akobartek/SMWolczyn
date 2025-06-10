package pl.kapucyni.wolczyn.app.meetings.presentation.meetings.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.domain.model.Meeting
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.meeting_groups
import smwolczyn.composeapp.generated.resources.meeting_name
import smwolczyn.composeapp.generated.resources.meeting_participants
import smwolczyn.composeapp.generated.resources.workshops

@Composable
fun MeetingCard(
    meeting: Meeting,
    userType: UserType,
    openParticipantsScreen: (Int) -> Unit,
    openWorkshopsScreen: (Int) -> Unit,
    openGroupsScreen: (Int) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
    ) {
        Column {
            AsyncImage(
                model = meeting.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
            ) {
                WolczynText(
                    text = stringResource(Res.string.meeting_name, meeting.id, meeting.name),
                    textStyle = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                )
                if (userType == UserType.ADMIN)
                    IconButton(onClick = { openWorkshopsScreen(meeting.id) }) {
                        Icon(
                            imageVector = Icons.Outlined.Construction,
                            contentDescription = stringResource(Res.string.workshops),
                        )
                    }
                if (userType == UserType.ADMIN && meeting.start.seconds <= Clock.System.now().epochSeconds)
                    IconButton(onClick = { openGroupsScreen(meeting.id) }) {
                        Icon(
                            imageVector = Icons.Outlined.Hub,
                            contentDescription = stringResource(Res.string.meeting_groups),
                        )
                    }
                IconButton(onClick = { openParticipantsScreen(meeting.id) }) {
                    Icon(
                        imageVector = Icons.Outlined.People,
                        contentDescription = stringResource(Res.string.meeting_participants),
                    )
                }
            }
        }
    }
}