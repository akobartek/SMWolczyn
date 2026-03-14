package pl.kapucyni.wolczyn.app.meetings.presentation.groups.composables

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.normalizeMultiplatform
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group

@Composable
fun GroupMembersList(
    title: String,
    members: Map<String, String>,
    currentGroup: Int?,
    allGroups: List<Group>,
    onMemberDialogSave: (Int?, String) -> Unit,
) {
    var memberDialogVisible: Pair<String, String>? by remember { mutableStateOf(null) }
    val sortedMembers by remember(members) {
        derivedStateOf { members.toList().sortedBy { it.second.normalizeMultiplatform() } }
    }

    WolczynText(
        text = title,
        modifier = Modifier.fillMaxWidth(),
    )
    sortedMembers.forEach { (email, data) ->
        WolczynText(
            text = "- $data",
            textStyle = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .combinedClickable(
                    onClick = { },
                    onLongClick = { memberDialogVisible = email to data },
                )
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        )
    }

    memberDialogVisible?.let { (email, data) ->
        GroupMemberDialog(
            email = email,
            data = data,
            currentGroup = currentGroup,
            allGroups = allGroups,
            onConfirm = onMemberDialogSave,
            onDismiss = { memberDialogVisible = null },
        )
    }
}