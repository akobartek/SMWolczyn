package pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsScreenState
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.group_members_title
import smwolczyn.composeapp.generated.resources.groups_not_available
import smwolczyn.composeapp.generated.resources.signing_confirmed
import smwolczyn.composeapp.generated.resources.user_group

@Composable
fun SigningsConfirmedScreen(
    state: SigningsScreenState,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        WolczynText(
            text = stringResource(Res.string.signing_confirmed, state.firstName),
            textStyle = MaterialTheme.typography.headlineSmall.copy(textAlign = Center),
        )
        HeightSpacer(20.dp)
        state.group?.let { group ->
            WolczynText(
                text = stringResource(Res.string.user_group),
                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = Center),
            )
            WolczynText(
                text = group.number.toString(),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    color = wolczynColors.primary,
                    fontWeight = Bold,
                ),
            )

            if (group.animatorMail == state.email) {
                HeightSpacer(12.dp)
                WolczynText(
                    text = stringResource(Res.string.group_members_title),
                    textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = Center),
                )
                HeightSpacer(4.dp)
                group.members.values.forEach { member ->
                    WolczynText(
                        text = "- $member",
                        textStyle = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                    )
                }
            }
        } ?: WolczynText(
            text = stringResource(Res.string.groups_not_available),
            textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = Center),
        )
        HeightSpacer(12.dp)
    }
}

