package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynGroup
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynUser
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.group_members_title
import smwolczyn.composeapp.generated.resources.groups_not_available
import smwolczyn.composeapp.generated.resources.user_group

@Composable
fun UserInfo(
    user: WolczynUser,
    group: WolczynGroup?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        WolczynText(
            text =
            "${user.prefix.getOrEmpty()}${user.name.getOrEmpty()}${user.surname.getOrEmpty()}",
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = wolczynColors.primary,
            ),
        )

        user.number?.let { number ->
            WolczynText(
                text = "ID: $number",
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = wolczynColors.primary,
                ),
            )
        }

        user.group?.let { groupNumber ->
            WolczynText(
                text = stringResource(Res.string.user_group, groupNumber),
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = TextAlign.Center,
                    color = wolczynColors.primary,
                ),
                modifier = Modifier.padding(top = 24.dp)
            )
        } ?: WolczynText(
            text = stringResource(Res.string.groups_not_available),
            textStyle = MaterialTheme.typography.titleMedium.copy(
                textAlign = TextAlign.Center,
                color = wolczynColors.primary,
            ),
            modifier = Modifier.padding(top = 40.dp)
        )

        group?.let { group ->
            WolczynText(
                text = stringResource(Res.string.group_members_title),
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = TextAlign.Center,
                    color = wolczynColors.primary,
                ),
                modifier = Modifier.padding(top = 16.dp)
            )
            val groupMembers = StringBuilder()
            group.persons?.forEach { member ->
                groupMembers.append(
                    "${member.name}, ${member.age} - ${member.city}\n"
                )
            }
            WolczynText(
                text = groupMembers.toString(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = TextAlign.Center,
                    color = wolczynColors.primary,
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

private fun String?.getOrEmpty() = this?.let { "$this " } ?: ""