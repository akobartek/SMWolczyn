package pl.kapucyni.wolczyn.app.meetings.presentation.groups.composables

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun GroupMembersList(
    title: String,
    members: HashMap<String, String>,
    currentGroup: Int?,
    allGroups: List<Group>,
    onMemberDialogSave: (Int, String) -> Unit,
) {
    var memberDialogVisible: Pair<String, String>? by remember { mutableStateOf(null) }

    WolczynText(
        text = title,
        modifier = Modifier.fillMaxWidth(),
    )
    members.forEach { (email, data) ->
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