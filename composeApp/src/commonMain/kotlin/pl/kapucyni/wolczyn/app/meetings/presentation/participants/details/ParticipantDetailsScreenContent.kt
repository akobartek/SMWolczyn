package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.CheckableField
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDate
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_cake
import smwolczyn.composeapp.generated.resources.ic_city
import smwolczyn.composeapp.generated.resources.ic_construction
import smwolczyn.composeapp.generated.resources.ic_email_alt
import smwolczyn.composeapp.generated.resources.ic_fingerprint
import smwolczyn.composeapp.generated.resources.ic_follow_the_signs
import smwolczyn.composeapp.generated.resources.ic_task_alt
import smwolczyn.composeapp.generated.resources.participant_group_body
import smwolczyn.composeapp.generated.resources.signing_confirm
import smwolczyn.composeapp.generated.resources.signing_confirm_consent
import smwolczyn.composeapp.generated.resources.signing_confirm_paid
import smwolczyn.composeapp.generated.resources.signing_confirm_underage_consent
import smwolczyn.composeapp.generated.resources.signing_confirmed_message
import smwolczyn.composeapp.generated.resources.workshops

@Composable
fun ParticipantDetailsScreenContent(
    participant: Participant,
    confirmUserSigning: () -> Unit,
    group: Group?,
    isConfirmed: Boolean,
) {
    var consent by rememberSaveable { mutableStateOf(false) }
    var underAgeConsent by rememberSaveable { mutableStateOf(participant.isUnderAge().not()) }
    var paid by rememberSaveable { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column {
            ParticipantInfo(
                imageVector = vectorResource(Res.drawable.ic_fingerprint),
                text = participant.pesel,
            )

            ParticipantInfo(
                imageVector = vectorResource(Res.drawable.ic_cake),
                text = participant.birthday.getFormattedDate(),
            )

            ParticipantInfo(
                imageVector = vectorResource(Res.drawable.ic_city),
                text = participant.city,
            )

            ParticipantInfo(
                imageVector = vectorResource(Res.drawable.ic_email_alt),
                text = participant.email,
            )

            ParticipantInfo(
                imageVector = vectorResource(Res.drawable.ic_follow_the_signs),
                text = stringResource(participant.type.stringRes),
            )

            if (participant.workshop.isNotBlank())
                ParticipantInfo(
                    imageVector = vectorResource(Res.drawable.ic_construction),
                    text = stringResource(Res.string.workshops) + ": " + participant.workshop,
                )

            ParticipantInfo(
                imageVector = vectorResource(Res.drawable.ic_task_alt),
                text = participant.createdAt.getFormattedDate(),
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .widthIn(max = 420.dp)
        ) {
            if (isConfirmed) {
                CheckableField(
                    checked = consent,
                    onCheckedChange = { consent = consent.not() },
                    text = stringResource(Res.string.signing_confirm_consent),
                )

                if (participant.isUnderAge())
                    CheckableField(
                        checked = underAgeConsent,
                        onCheckedChange = { underAgeConsent = underAgeConsent.not() },
                        text = stringResource(Res.string.signing_confirm_underage_consent),
                    )

                CheckableField(
                    checked = paid,
                    onCheckedChange = { paid = paid.not() },
                    text = stringResource(Res.string.signing_confirm_paid),
                )

                Button(
                    onClick = {
                        if (consent && underAgeConsent && paid)
                            confirmUserSigning()
                    },
                    enabled = consent && underAgeConsent && paid,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    WolczynText(text = stringResource(Res.string.signing_confirm))
                }
            } else {
                WolczynText(
                    text = stringResource(Res.string.signing_confirmed_message),
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        textAlign = TextAlign.Center,
                    ),
                )
                group?.let {
                    HeightSpacer(12.dp)
                    WolczynText(
                        text = stringResource(
                            Res.string.participant_group_body,
                            group.number,
                        ),
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        }

        HeightSpacer(12.dp)
    }
}

@Composable
private fun ParticipantInfo(
    imageVector: ImageVector,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        WidthSpacer(8.dp)
        WolczynText(
            text = text,
            textStyle = MaterialTheme.typography.bodyLarge,
        )
    }
}