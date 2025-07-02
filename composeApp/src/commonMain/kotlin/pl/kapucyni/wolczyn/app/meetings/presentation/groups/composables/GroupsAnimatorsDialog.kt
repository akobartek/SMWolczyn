package pl.kapucyni.wolczyn.app.meetings.presentation.groups.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType.ANIMATOR
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.draw
import smwolczyn.composeapp.generated.resources.select_animators

@Composable
fun GroupsAnimatorsDialog(
    isVisible: Boolean,
    animators: List<Participant>,
    selectedAnimators: List<Participant>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onAnimatorClicked: (Participant, Boolean) -> Unit,
) {
    if (isVisible)
        AlertDialog(
            icon = {
                Icon(
                    imageVector = Icons.Filled.SupervisedUserCircle,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            title = {
                WolczynText(
                    text = stringResource(Res.string.select_animators),
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Center,
                    ),
                )
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(items = animators, key = { it.email }) { animator ->
                        val selected = selectedAnimators.contains(animator)
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (animator.type != ANIMATOR)
                                        onAnimatorClicked(animator, selected.not())
                                }
                                .background(
                                    color =
                                        if (selected) MaterialTheme.colorScheme.secondaryContainer
                                        else MaterialTheme.colorScheme.background,
                                )
                                .padding(8.dp),
                        ) {
                            WolczynText(
                                text = stringResource(animator.type.stringRes),
                                textStyle = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.outline,
                                ),
                            )
                            WolczynText(
                                text = "${animator.firstName} ${animator.lastName}",
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color =
                                        if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                                        else MaterialTheme.colorScheme.onBackground,
                                ),
                            )
                        }
                    }
                }
            },
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(stringResource(Res.string.draw))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(Res.string.cancel))
                }
            },
            modifier = Modifier.heightIn(max = 560.dp),
        )
}