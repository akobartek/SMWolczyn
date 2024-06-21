package pl.kapucyni.wolczyn.app.archive.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.archive_title

@Composable
fun ArchiveScreen(
    onBackPressed: () -> Unit,
    viewModel: ArchiveViewModel = koinInject()
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()

    ScreenLayout(
        title = stringResource(Res.string.archive_title),
        onBackPressed = onBackPressed
    ) {
        ArchiveScreenContent(
            screenState = screenState,
        )
    }
}

@Composable
fun ArchiveScreenContent(
    screenState: State<List<ArchiveMeeting>>
) {

}