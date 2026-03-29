package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.firestore.Timestamp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDate
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import pl.kapucyni.wolczyn.app.theme.AppTheme

@Composable
fun ParticipantCard(
    participant: Participant,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors =
            if (participant.paid) CardDefaults.cardColors()
            else CardDefaults.elevatedCardColors(),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
        ),
        modifier = Modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick,
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                WolczynText(
                    text = stringResource(participant.type.stringRes),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.outline,
                    ),
                    modifier = Modifier.weight(1f),
                )
                WidthSpacer(12.dp)
                WolczynText(
                    text = participant.city,
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.outline,
                    ),
                )
            }
            SelectionContainer {
                WolczynText(
                    text = "${participant.firstName} ${participant.lastName}",
                    textStyle = MaterialTheme.typography.titleLarge,
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    SelectionContainer {
                        WolczynText(
                            text = participant.email,
                            textStyle = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.outline,
                            ),
                        )
                    }
                }
                WidthSpacer(12.dp)
                WolczynText(
                    text = participant.birthday.getFormattedDate(),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.outline,
                    ),
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = AndroidUiModes.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ParticipantCardPreview() {
    AppTheme {
        ParticipantCard(
            participant = Participant(
                firstName = "Jan",
                lastName = "Kowalski",
                pesel = "12345678901",
                city = "Warszawa",
                birthday = Timestamp.now(),
                email = "test@test.com",
                contactNumber = "123456789",
                paid = false,
                type = ParticipantType.MEMBER,
                createdAt = Timestamp.now(),
            ),
            onClick = {},
            onLongClick = {},
        )
    }
}