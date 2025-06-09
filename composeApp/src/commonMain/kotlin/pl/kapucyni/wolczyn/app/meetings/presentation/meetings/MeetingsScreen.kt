package pl.kapucyni.wolczyn.app.meetings.presentation.meetings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmptyListInfo
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.meetings.domain.model.Meeting
import pl.kapucyni.wolczyn.app.meetings.presentation.meetings.composables.MeetingCard
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.empty_meetings_list
import smwolczyn.composeapp.generated.resources.ic_cap_archive
import smwolczyn.composeapp.generated.resources.meetings
import smwolczyn.composeapp.generated.resources.signings_title

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MeetingsScreen(
    navigateUp: () -> Unit,
    navigate: (Screen) -> Unit,
    userType: UserType,
    openSigningMeeting: Int?,
    viewModel: MeetingsViewModel = koinViewModel(),
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    if (userType == UserType.MEMBER)
        return navigateUp()

    ScreenLayout(
        title = stringResource(Res.string.meetings),
        onBackPressed = navigateUp,
        actionIcon = openSigningMeeting?.let {
            {
                IconButton(onClick = { navigate(Screen.Signings()) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.NoteAdd,
                        tint = wolczynColors.primary,
                        contentDescription = stringResource(Res.string.signings_title),
                    )
                }
            }
        }
    ) {
        when (state) {
            is State.Loading -> LoadingBox()
            is State.Success -> (state as? State.Success)?.data?.let { meetings ->
                MeetingsScreenContent(
                    meetings = meetings,
                    userType = userType,
                    openParticipantsScreen = { navigate(Screen.MeetingParticipants(it)) },
                    openWorkshopsScreen = { navigate(Screen.MeetingWorkshops(it)) },
                    openGroupsScreen = { navigate(Screen.MeetingGroups(it)) },
                )
            } ?: LoadingBox()
        }
    }
}

@Composable
private fun MeetingsScreenContent(
    meetings: List<Meeting>,
    userType: UserType,
    openParticipantsScreen: (Int) -> Unit,
    openWorkshopsScreen: (Int) -> Unit,
    openGroupsScreen: (Int) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(items = meetings, key = { it.id }) { meeting ->
            MeetingCard(
                meeting = meeting,
                userType = userType,
                openParticipantsScreen = openParticipantsScreen,
                openWorkshopsScreen = openWorkshopsScreen,
                openGroupsScreen = openGroupsScreen,
            )
        }

        if (meetings.isEmpty())
            item {
                EmptyListInfo(
                    messageRes = Res.string.empty_meetings_list,
                    drawableRes = Res.drawable.ic_cap_archive,
                )
            }
    }
}