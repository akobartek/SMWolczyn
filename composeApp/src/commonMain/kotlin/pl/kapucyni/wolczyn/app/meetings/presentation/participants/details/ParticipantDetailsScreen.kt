package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.ObserveAsEvents
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.details.ParticipantDetailsScreenEvent.NavigateUp

@Composable
fun ParticipantDetailsScreen(
    navigateUp: () -> Unit,
    viewModel: ParticipantDetailsViewModel,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is NavigateUp -> navigateUp()
        }
    }

    ScreenLayout(
        title = null,
        onBackPressed = navigateUp,
    ) {
        when (state) {
            is State.Loading -> LoadingBox()
            is State.Success -> (state as? State.Success)?.data?.let { participant ->

            } ?: LoadingBox()
        }
    }
}