package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import pl.kapucyni.wolczyn.app.common.presentation.ObserveAsEvents
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.meetings.presentation.participants.details.ParticipantDetailsScreenEvent.NavigateUp

@Composable
fun ParticipantDetailsScreen(
    navigateUp: () -> Unit,
    viewModel: ParticipantDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val group by viewModel.group.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is NavigateUp -> navigateUp()
        }
    }

    LoadingDialog(visible = loading)

    ScreenLayout(
        title = state?.participant?.let { "${it.firstName} ${it.lastName}" }.orEmpty(),
        onBackPressed = navigateUp,
    ) {
        state?.let {
            ParticipantDetailsScreenContent(
                participant = it.participant,
                showData = it.showData,
                meetingsCount = it.meetingsCount,
                confirmUserSigning = viewModel::confirmUserSigning,
                group = group,
            )
        } ?: LoadingBox()
    }
}