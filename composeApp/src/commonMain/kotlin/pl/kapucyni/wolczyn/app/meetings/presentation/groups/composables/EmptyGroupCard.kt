package pl.kapucyni.wolczyn.app.meetings.presentation.groups.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.members_without_group_title

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmptyGroupCard(
    numberOfGroups: Int,
    members: HashMap<String, String>,
    onMemberDialogSave: (Int, String) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            GroupMembersList(
                title = stringResource(Res.string.members_without_group_title),
                members = members,
                currentGroup = null,
                numberOfGroups = numberOfGroups,
                onMemberDialogSave = onMemberDialogSave,
            )
        }
    }
}