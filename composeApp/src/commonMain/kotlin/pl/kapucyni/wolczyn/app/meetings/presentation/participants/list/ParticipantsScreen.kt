package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import pl.kapucyni.wolczyn.app.common.presentation.ObserveAsEvents
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmptyListInfo
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynFabMenu
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynFloatingActionButton
import pl.kapucyni.wolczyn.app.common.presentation.fab.FloatingButtonData
import pl.kapucyni.wolczyn.app.common.utils.CodeScanner
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.QrScanFailure
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.QrScanSuccess
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.QrScannerNotFound
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenEvent.NavigateUp
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenEvent.ScanUserFound
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.composables.ParticipantCard
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.composables.ParticipantsFilteringBottomSheet
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.composables.SimplifiedParticipantCard
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.add_participant
import smwolczyn.composeapp.generated.resources.cd_qr_scanner
import smwolczyn.composeapp.generated.resources.cd_send_email
import smwolczyn.composeapp.generated.resources.empty_participants_list
import smwolczyn.composeapp.generated.resources.empty_participants_list_v2
import smwolczyn.composeapp.generated.resources.filter_participants
import smwolczyn.composeapp.generated.resources.ic_add
import smwolczyn.composeapp.generated.resources.ic_cap_archive
import smwolczyn.composeapp.generated.resources.ic_email
import smwolczyn.composeapp.generated.resources.ic_filter
import smwolczyn.composeapp.generated.resources.ic_qr_scanner
import smwolczyn.composeapp.generated.resources.meeting_participants

@Composable
fun ParticipantsScreen(
    navigateUp: () -> Unit,
    navigate: (Screen) -> Unit,
    viewModel: ParticipantsViewModel = koinViewModel(),
    codeScanner: CodeScanner = koinInject(),
) {
    val uriHandler = LocalUriHandler.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()

    var filterSheetVisible by remember { mutableStateOf(false) }
    var fabVisible by remember { mutableStateOf(true) }

    val openDetails = { participant: Participant, forceDetails: Boolean ->
        state?.let { state ->
            if (state.user.canEditParticipantsData() && forceDetails.not())
                navigate(Screen.SigningsAdmin(state.meetingId, participant))
            else
                navigate(Screen.ParticipantDetails(state.meetingId, participant))
        }
    }
    val startScanning = {
        try {
            codeScanner.startScanning(
                onSuccess = {
                    viewModel.handleAction(QrScanSuccess(email = it))
                },
                onFailure = { viewModel.handleAction(QrScanFailure(invalidValue = it)) },
                onCancel = {},
                onNoScannerFound = { viewModel.handleAction(QrScannerNotFound) },
            )
        } catch (_: Exception) {
            viewModel.handleAction(QrScanFailure(invalidValue = false))
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ScanUserFound -> openDetails(event.participant, true)
            is NavigateUp -> navigateUp()
        }
    }

    ScreenLayout(
        title = stringResource(Res.string.meeting_participants) +
                (state?.let { state -> state.participants.size.takeIf { it > 0 }?.let { "($it)" } }
                    ?: ""),
        onBackPressed = navigateUp,
        actionIcon =
            if (codeScanner.available) {
                {
                    IconButton(onClick = startScanning) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_qr_scanner),
                            tint = wolczynColors.primary,
                            contentDescription = stringResource(Res.string.cd_qr_scanner),
                        )
                    }
                }
            } else null,
        floatingActionButton = {
            state?.let { state ->
                if (state.dataVisible) {
                    WolczynFabMenu(
                        visible = fabVisible,
                        items = listOf(
                            FloatingButtonData(
                                icon = Res.drawable.ic_email,
                                contentDescription = Res.string.cd_send_email,
                                onClick = {
                                    state.participants
                                        .joinToString(",") { it.email }
                                        .takeIf { it.isNotBlank() }
                                        ?.let { emails ->
                                            uriHandler.openUri("mailto:?bcc=$emails")
                                        }
                                },
                                isSmall = true,
                                enabled = state.participants.isNotEmpty(),
                            ),
                            FloatingButtonData(
                                icon = Res.drawable.ic_filter,
                                contentDescription = Res.string.filter_participants,
                                onClick = { filterSheetVisible = true },
                                isSmall = true,
                            ),
                            FloatingButtonData(
                                icon = Res.drawable.ic_add,
                                contentDescription = Res.string.add_participant,
                                onClick = { navigate(Screen.SigningsAdmin(state.meetingId)) },
                                isSmall = true,
                                enabled = state.user.isAdmin(),
                            ),
                        ),
                    )
                } else {
                    WolczynFloatingActionButton(
                        data = FloatingButtonData(
                            icon = Res.drawable.ic_filter,
                            contentDescription = Res.string.filter_participants,
                            onClick = { filterSheetVisible = true },
                        ),
                    )
                }
            }
        }
    ) {
        state?.let { state ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp)
                    .nestedScroll(object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource,
                        ): Offset {
                            when {
                                available.y > 1 -> fabVisible = true
                                available.y < -1 -> fabVisible = false
                            }
                            return Offset.Zero
                        }
                    }),
            ) {
                items(items = state.participants, key = { it.email }) { participant ->
                    if (state.dataVisible) {
                        ParticipantCard(
                            participant = participant,
                            onClick = { openDetails(participant, true) },
                            onLongClick = { openDetails(participant, false) },
                        )
                    } else {
                        SimplifiedParticipantCard(
                            participant = participant,
                            onClick = { openDetails(participant, true) },
                        )
                    }
                }

                if (state.participants.isEmpty())
                    item {
                        EmptyListInfo(
                            messageRes =
                                if (state.dataVisible) Res.string.empty_participants_list
                                else Res.string.empty_participants_list_v2,
                            drawableRes = Res.drawable.ic_cap_archive,
                        )
                    }
            }
        } ?: LoadingBox()
    }

    ParticipantsFilteringBottomSheet(
        state = filterState,
        handleAction = viewModel::handleAction,
        isVisible = filterSheetVisible,
        hasDataAccess = state?.user?.hasAccessToParticipantsData() == true,
        onDismiss = { filterSheetVisible = false },
    )
}