package pl.kapucyni.wolczyn.app.songbook.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform

@Composable
fun SongBookScreen(viewModel: SongBookViewModel = koinInject()) {
    val state by viewModel.screenState.collectAsStateMultiplatform()
    val query by viewModel.searchQuery.collectAsStateMultiplatform()
}