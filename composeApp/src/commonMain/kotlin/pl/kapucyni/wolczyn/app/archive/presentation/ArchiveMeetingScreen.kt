package pl.kapucyni.wolczyn.app.archive.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveMeetingScreenEvent.NavigateUp
import pl.kapucyni.wolczyn.app.archive.presentation.composables.ArchiveRecordCard
import pl.kapucyni.wolczyn.app.common.presentation.ObserveAsEvents
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_music_note

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ArchiveMeetingScreen(
    navigateUp: () -> Unit,
    viewModel: ArchiveMeetingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is NavigateUp -> navigateUp()
        }
    }

    ScreenLayout(
        title = state?.name?.split(" - ")?.lastOrNull() ?: "",
        onBackPressed = navigateUp,
        actionIcon = {
            state?.anthem?.takeIf { it.isNotBlank() }?.let { anthem ->
                IconButton(onClick = { uriHandler.openUri(anthem) }) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_music_note),
                        tint = wolczynColors.primary,
                        contentDescription = null
                    )
                }
            } ?: WidthSpacer(40.dp)
        }
    ) {
        state?.let { meeting ->
            ArchiveMeetingScreenContent(meeting = meeting)
        } ?: LoadingBox()
    }
}

@Composable
fun ArchiveMeetingScreenContent(
    meeting: ArchiveMeeting,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = meeting.records, key = { it.name }) { record ->
            ArchiveRecordCard(record = record)
        }
    }
}