package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.QrCodeScanner
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
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType.ANIMATORS_MANAGER
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType.SCOUTS_MANAGER
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.ObserveAsEvents
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmptyListInfo
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynFabMenu
import pl.kapucyni.wolczyn.app.common.presentation.fab.FloatingButtonData
import pl.kapucyni.wolczyn.app.common.utils.CodeScanner
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.QrScanFailure
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.ParticipantsScreenAction.QrScanSuccess
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.composables.ParticipantCard
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.list.composables.ParticipantsFilteringBottomSheet
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.add_participant
import smwolczyn.composeapp.generated.resources.cd_qr_scanner
import smwolczyn.composeapp.generated.resources.cd_send_email
import smwolczyn.composeapp.generated.resources.empty_participants_list
import smwolczyn.composeapp.generated.resources.filter_participants
import smwolczyn.composeapp.generated.resources.ic_cap_archive
import smwolczyn.composeapp.generated.resources.meeting_animators
import smwolczyn.composeapp.generated.resources.meeting_participants
import smwolczyn.composeapp.generated.resources.meeting_scouts

@Composable
fun ParticipantsScreen(
    navigateUp: () -> Unit,
    navigate: (Screen) -> Unit,
    userType: UserType,
    viewModel: ParticipantsViewModel,
    codeScanner: CodeScanner = koinInject(),
) {
    val uriHandler = LocalUriHandler.current

    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()

    var filterSheetVisible by remember { mutableStateOf(false) }
    var fabVisible by remember { mutableStateOf(true) }
    val openDetails = { participant: Participant ->
        if (userType == UserType.ADMIN)
            navigate(Screen.Signings(isAdmin = true, email = participant.email))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ParticipantsScreenEvent.QrScanSuccess -> openDetails(event.participant)
        }
    }

    ScreenLayout(
        title = stringResource(
            when (userType) {
                SCOUTS_MANAGER -> Res.string.meeting_scouts
                ANIMATORS_MANAGER -> Res.string.meeting_animators
                else -> Res.string.meeting_participants
            }
        ) + ((state as? State.Success)?.data?.let { " (${it.size})" } ?: ""),
        onBackPressed = navigateUp,
        actionIcon =
            if (codeScanner.available && userType.canManageParticipants()) {
                {
                    IconButton(onClick = {
                        codeScanner.startScanning(
                            onSuccess = { viewModel.handleAction(QrScanSuccess(it)) },
                            onFailure = { viewModel.handleAction(QrScanFailure) },
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.QrCodeScanner,
                            tint = wolczynColors.primary,
                            contentDescription = stringResource(Res.string.cd_qr_scanner),
                        )
                    }
                }
            } else null,
        floatingActionButton = {
            val successData = (state as? State.Success)?.data
            WolczynFabMenu(
                visible = fabVisible,
                items = listOf(
                    FloatingButtonData(
                        icon = Icons.Default.Email,
                        contentDescription = Res.string.cd_send_email,
                        onClick = {
                            successData?.joinToString(",") { it.email }
                                ?.takeIf { it.isNotBlank() }
                                ?.let { emails ->
                                    uriHandler.openUri("mailto:?bcc=$emails")
                                }
                        },
                        isSmall = true,
                        enabled = (successData?.isNotEmpty() == true),
                    ),
                    FloatingButtonData(
                        icon = Icons.Default.FilterAlt,
                        contentDescription = Res.string.filter_participants,
                        onClick = { filterSheetVisible = true },
                        isSmall = true,
                        enabled = userType.canManageParticipants(),
                    ),
                    FloatingButtonData(
                        icon = Icons.Default.Add,
                        contentDescription = Res.string.add_participant,
                        onClick = { navigate(Screen.Signings(isAdmin = true)) },
                        isSmall = true,
                        enabled = userType.canManageParticipants(),
                    ),
                ),
            )
        }
    ) {
        when (state) {
            is State.Loading -> LoadingBox()
            is State.Success -> (state as? State.Success)?.data?.let { participants ->
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
                    items(items = participants, key = { it.pesel }) { participant ->
                        ParticipantCard(
                            participant = participant,
                            onClick = {
                                if (userType.canManageParticipants())
                                    openDetails(participant)
                            },
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
            } ?: LoadingBox()
        }
    }

    ParticipantsFilteringBottomSheet(
        state = filterState,
        handleAction = viewModel::handleAction,
        isVisible = filterSheetVisible,
        onDismiss = { filterSheetVisible = false },
    )
}