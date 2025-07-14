package pl.kapucyni.wolczyn.app.meetings.presentation.groups.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.group_members_title
import smwolczyn.composeapp.generated.resources.participant_type_animator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroupCard(
    group: Group,
    allGroups: List<Group>,
    onAnimatorDialogSave: (Int, String) -> Unit,
    onMemberDialogSave: (Int, String) -> Unit,
) {
    var animatorDialogVisible by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            WolczynText(
                text = group.number.toString(),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = ExtraBold,
                ),
            )
            WolczynText(
                text = stringResource(Res.string.participant_type_animator) + ": ${group.animatorName}",
                textStyle = MaterialTheme.typography.titleSmall.copy(
                    textAlign = Center,
                ),
                modifier = Modifier
                    .clickable { animatorDialogVisible = true }
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
            )
            HeightSpacer(12.dp)
            GroupMembersList(
                title = stringResource(Res.string.group_members_title),
                members = group.members,
                currentGroup = group.number,
                allGroups = allGroups,
                onMemberDialogSave = onMemberDialogSave,
            )
        }
    }

    AnimatorDataDialog(
        isVisible = animatorDialogVisible,
        name = group.animatorName,
        contact = group.animatorContact,
        currentGroup = group.number,
        allGroups = allGroups,
        onConfirm = onAnimatorDialogSave,
        onDismiss = { animatorDialogVisible = false },
    )
}