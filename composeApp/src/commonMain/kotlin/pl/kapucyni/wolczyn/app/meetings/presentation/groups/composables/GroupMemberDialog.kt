package pl.kapucyni.wolczyn.app.meetings.presentation.groups.composables

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.SelectableTextView
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.current_group
import smwolczyn.composeapp.generated.resources.save

@Composable
fun GroupMemberDialog(
    email: String,
    data: String,
    currentGroup: Int?,
    allGroups: List<Group>,
    onConfirm: (Int, String) -> Unit,
    onDismiss: () -> Unit,
) {
    var newGroup by rememberSaveable { mutableStateOf(currentGroup) }

    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        title = {
            WolczynText(
                text = data,
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = TextAlign.Center,
                ),
            )
        },
        text = {
            SelectableTextView(
                value = newGroup?.toString() ?: "",
                label = Res.string.current_group,
                items = allGroups.map { it.number to "${it.number} (${it.animatorName})" },
                onItemSelected = { newGroup = it },
            )
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                newGroup?.let {
                    if (it != currentGroup)
                        onConfirm(it, email)
                }
            }) {
                Text(stringResource(Res.string.save))
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