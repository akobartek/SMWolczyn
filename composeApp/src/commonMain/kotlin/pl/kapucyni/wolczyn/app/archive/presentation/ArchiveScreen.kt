package pl.kapucyni.wolczyn.app.archive.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.presentation.composables.ArchiveMeetingCard
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.archive_title

@Composable
fun ArchiveScreen(
    onBackPressed: () -> Unit,
    onMeetingClick: (Int) -> Unit,
    viewModel: ArchiveViewModel = koinInject()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    ScreenLayout(
        title = stringResource(Res.string.archive_title),
        onBackPressed = onBackPressed
    ) {
        ArchiveScreenContent(
            screenState = screenState,
            onMeetingClick = onMeetingClick
        )
    }
}

@Composable
fun ArchiveScreenContent(
    screenState: State<List<ArchiveMeeting>>,
    onMeetingClick: (Int) -> Unit = {}
) {

    when (screenState) {
        is State.Loading -> LoadingBox()
        is State.Success -> {
            val archive = screenState.data
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 12.dp)
            ) {
                items(
                    count = archive.size,
                    key = { archive[it].number }
                ) { index ->
                    ArchiveMeetingCard(
                        meeting = archive[index],
                        onMeetingClick = onMeetingClick
                    )
                }
            }
        }
    }
}