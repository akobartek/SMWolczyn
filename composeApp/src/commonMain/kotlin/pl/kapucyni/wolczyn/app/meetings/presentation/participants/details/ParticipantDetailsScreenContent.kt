package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.TaskAlt
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
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.CheckableField
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDate
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.signing_confirm
import smwolczyn.composeapp.generated.resources.signing_confirm_consent
import smwolczyn.composeapp.generated.resources.signing_confirm_paid
import smwolczyn.composeapp.generated.resources.signing_confirm_underage_consent

@Composable
fun ParticipantDetailsScreenContent(
    participant: Participant,
    confirmUserSigning: () -> Unit,
) {
    var consent by rememberSaveable { mutableStateOf(false) }
    var underAgeConsent by rememberSaveable { mutableStateOf(participant.isUnderAge().not()) }
    var paid by rememberSaveable { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column {
            ParticipantInfo(
                imageVector = Icons.Outlined.Fingerprint,
                text = participant.pesel,
            )

            ParticipantInfo(
                imageVector = Icons.Outlined.Cake,
                text = participant.birthday.getFormattedDate(),
            )

            ParticipantInfo(
                imageVector = Icons.Outlined.LocationCity,
                text = participant.city,
            )

            ParticipantInfo(
                imageVector = Icons.Outlined.AlternateEmail,
                text = participant.email,
            )

            ParticipantInfo(
                imageVector = Icons.Outlined.TaskAlt,
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
        }

        HeightSpacer(4.dp)
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