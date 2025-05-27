package pl.kapucyni.wolczyn.app.meetings.presentation.participants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmptyListInfo
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynBottomSheet
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynFloatingActionButton
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.composables.ParticipantCard
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.add_participant
import smwolczyn.composeapp.generated.resources.empty_participants_list
import smwolczyn.composeapp.generated.resources.filter_participants
import smwolczyn.composeapp.generated.resources.ic_cap_archive
import smwolczyn.composeapp.generated.resources.meeting_participants

@Composable
fun ParticipantsScreen(
    navigateUp: () -> Unit,
    navigate: (Screen) -> Unit,
    openSigningMeeting: Int?,
    viewModel: ParticipantsViewModel,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    var filterSheetVisible by remember { mutableStateOf(false) }

    ScreenLayout(
        title = stringResource(Res.string.meeting_participants)
                + ((state as? State.Success)?.data?.let { " (${it.size})" } ?: ""),
        onBackPressed = navigateUp,
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                WolczynFloatingActionButton(
                    onClick = { filterSheetVisible = true },
                    imageVector = Icons.Default.FilterAlt,
                    contentDescription = Res.string.filter_participants,
                )
                WolczynFloatingActionButton(
                    onClick = { navigate(Screen.Signings(isAdmin = true)) },
                    imageVector = Icons.Default.Add,
                    contentDescription = Res.string.add_participant,
                )
            }
        }
    ) {
        when (state) {
            is State.Loading -> LoadingBox()
            is State.Success -> (state as? State.Success)?.data?.let { participants ->
                MeetingsScreenContent(
                    participants = participants,
                )
            } ?: LoadingBox()
        }
    }

    WolczynBottomSheet(
        isVisible = filterSheetVisible,
        onDismiss = { filterSheetVisible = false },
    ) {

    }
}

@Composable
private fun MeetingsScreenContent(
    participants: List<Participant>,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(items = participants, key = { it.pesel }) { participant ->
            ParticipantCard(
                participant = participant,
            )
        }

        if (participants.isEmpty())
            item {
                EmptyListInfo(
                    messageRes = Res.string.empty_participants_list,
                    drawableRes = Res.drawable.ic_cap_archive,
                )
            }
    }
}