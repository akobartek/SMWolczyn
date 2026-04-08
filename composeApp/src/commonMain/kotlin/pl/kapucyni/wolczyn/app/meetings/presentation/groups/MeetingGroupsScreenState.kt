package pl.kapucyni.wolczyn.app.meetings.presentation.groups

import androidx.compose.runtime.Immutable
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant

@Immutable
data class MeetingGroupsScreenState(
    val loading: Boolean = false,
    val newGroups: List<Group>,
    val savedGroups: List<Group>,
    val membersWithoutGroup: Map<String, String>,
    val participants: List<Participant>,
    val potentialAnimators: List<Participant>,
    val selectedAnimators: List<Participant>,
    val saveAvailable: Boolean = false,
    val copyAvailable: Boolean = false,
    val animatorsDialogVisible: Boolean = false,
)
