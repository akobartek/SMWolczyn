package pl.kapucyni.wolczyn.app.archive.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.presentation.composables.ArchiveRecordCard
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.theme.wolczynColors

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ArchiveMeetingScreen(
    onBackPressed: () -> Unit,
    meetingNumber: Int,
    viewModel: ArchiveMeetingViewModel = koinViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val state = screenState
    if (state is State.Success && state.data == null)
        onBackPressed()

    LaunchedEffect(Unit) {
        viewModel.init(meetingNumber)
    }
    val uriHandler = LocalUriHandler.current

    ScreenLayout(
        title = when (state) {
            is State.Loading -> ""
            is State.Success -> state.data?.name?.split(" - ")?.lastOrNull() ?: ""
        },
        onBackPressed = onBackPressed,
        actionIcon = {
            if (state is State.Success && !state.data?.anthem.isNullOrBlank())
                IconButton(onClick = { uriHandler.openUri(state.data?.anthem ?: "") }) {
                    Icon(
                        imageVector = Icons.Filled.MusicNote,
                        tint = wolczynColors.primary,
                        contentDescription = null
                    )
                }
            else WidthSpacer(40.dp)
        }
    ) {
        ArchiveMeetingScreenContent(
            screenState = screenState,
        )
    }
}

@Composable
fun ArchiveMeetingScreenContent(
    screenState: State<ArchiveMeeting?>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        when (screenState) {
            is State.Loading -> item { LoadingBox() }
            is State.Success -> {
                val records = screenState.data?.records ?: listOf()
                items(items = records, key = { it.name }) { record ->
                    ArchiveRecordCard(record = record)
                }
            }
        }
    }
}