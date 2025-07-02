package pl.kapucyni.wolczyn.app.meetings.presentation.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.DrawGroups
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnAnimatorClicked
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.ToggleAnimatorsDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.composables.GroupsAnimatorsDialog
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.draw_groups
import smwolczyn.composeapp.generated.resources.empty_groups_list

@Composable
fun MeetingGroupsScreenContent(
    state: MeetingGroupsScreenState,
    handleAction: (MeetingGroupsScreenAction) -> Unit,
) {
    if (state.newGroups.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth(),
        ) {
            WolczynText(
                text = stringResource(Res.string.empty_groups_list),
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Center,
                ),
            )
            Button(
                onClick = { handleAction(ToggleAnimatorsDialog) },
            ) {
                WolczynText(text = stringResource(Res.string.draw_groups))
            }
        }
    } else {
        // TODO
    }

    LoadingDialog(visible = state.loading)

    GroupsAnimatorsDialog(
        isVisible = state.animatorsDialogVisible,
        animators = state.potentialAnimators,
        selectedAnimators = state.selectedAnimators,
        onDismiss = { handleAction(ToggleAnimatorsDialog) },
        onConfirm = {
            handleAction(ToggleAnimatorsDialog)
            handleAction(DrawGroups)
        },
        onAnimatorClicked = { participant, selected ->
            handleAction(OnAnimatorClicked(participant, selected))
        },
    )
}