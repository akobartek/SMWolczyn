package pl.kapucyni.wolczyn.app.meetings.presentation.groups.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnAnimatorDataChange
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnMemberGroupAdd
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.OnMemberGroupChange
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenState

@Composable
fun GroupsGrid(
    modifier: Modifier = Modifier,
    state: MeetingGroupsScreenState,
    handleAction: (MeetingGroupsScreenAction) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(280.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
    ) {
        if (state.membersWithoutGroup.isNotEmpty())
            item(span = { GridItemSpan(1) }) {
                EmptyGroupCard(
                    numberOfGroups = state.newGroups.size,
                    members = state.membersWithoutGroup,
                    onMemberDialogSave = { number, email ->
                        handleAction(
                            OnMemberGroupAdd(
                                groupNumber = number,
                                email = email,
                            )
                        )
                    },
                )
            }

        items(
            count = state.newGroups.size,
            key = { it },
            span = { GridItemSpan(1) },
        ) { index ->
            val group = state.newGroups[index]
            GroupCard(
                group = group,
                numberOfGroups = state.newGroups.size,
                onAnimatorDialogSave = { number, contact ->
                    handleAction(
                        OnAnimatorDataChange(
                            currentGroupNumber = group.number,
                            groupNumber = number,
                            contactNumber = contact,
                        )
                    )
                },
                onMemberDialogSave = { number, email ->
                    handleAction(
                        OnMemberGroupChange(
                            currentGroupNumber = group.number,
                            groupNumber = number,
                            email = email,
                        )
                    )
                },
            )
        }
    }
}