package pl.kapucyni.wolczyn.app.workshops.prayer.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmptyListInfo
import pl.kapucyni.wolczyn.app.common.presentation.composables.FullScreenDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.model.PrayerWorkshopTask
import pl.kapucyni.wolczyn.app.workshops.prayer.presentation.composables.PrayerTaskCard
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.empty_workshop_tasks_list
import smwolczyn.composeapp.generated.resources.ic_cap_schedule
import smwolczyn.composeapp.generated.resources.workshops_prayer_title

@Composable
fun PrayerWorkshopDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    viewModel: PrayerWorkshopViewModel = koinInject(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    PrayerWorkshopDialogContent(
        isVisible = isVisible,
        onDismiss = onDismiss,
        state = screenState,
    )
}

@Composable
fun PrayerWorkshopDialogContent(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    state: State<List<PrayerWorkshopTask>>,
) {
    FullScreenDialog(
        isVisible = isVisible,
        title = stringResource(Res.string.workshops_prayer_title),
        onAction = null,
        onDismiss = onDismiss,
    ) {
        if (state is State.Loading) LoadingBox()
        else
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(items = (state as State.Success).data, key = { it.name }) { task ->
                    PrayerTaskCard(task = task)
                }

                if (state.data.isEmpty())
                    item {
                        EmptyListInfo(
                            messageRes = Res.string.empty_workshop_tasks_list,
                            drawableRes = Res.drawable.ic_cap_schedule,
                        )
                    }

                item {
                    HeightSpacer(12.dp)
                }
            }
    }
}
