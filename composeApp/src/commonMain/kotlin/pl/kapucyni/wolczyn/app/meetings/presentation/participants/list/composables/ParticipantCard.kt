package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDate
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant

@Composable
fun ParticipantCard(
    participant: Participant,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
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