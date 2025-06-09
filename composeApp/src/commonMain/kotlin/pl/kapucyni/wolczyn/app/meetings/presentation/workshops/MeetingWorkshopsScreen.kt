package pl.kapucyni.wolczyn.app.meetings.presentation.workshops

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsScreenAction.SaveWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsScreenAction.UpdateIsAdding
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.MeetingWorkshopsScreenAction.UpdateWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.composables.WorkshopCard
import pl.kapucyni.wolczyn.app.meetings.presentation.workshops.composables.WorkshopNewTextField
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.workshops_title

@Composable
fun MeetingWorkshopsScreen(
    navigateUp: () -> Unit,
    viewModel: MeetingWorkshopsViewModel,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val isAdding by viewModel.isAdding.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    ScreenLayout(
        title = stringResource(Res.string.workshops_title),
        onBackPressed = navigateUp,
        floatingActionButton = {
            if (state is State.Success) {
                val animatedRotation: Float by animateFloatAsState(
                    if (isAdding.not()) 0f else 45f,
                    label = "rotation",
                )
                FloatingActionButton(onClick = {
                    viewModel.handleAction(UpdateIsAdding(isAdding.not()))
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = animatedRotation
                        },
                    )
                }
            }
        },
    ) {
        when (state) {
            is State.Loading -> LoadingBox()
            is State.Success -> (state as? State.Success)?.data?.let { pairs ->
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                ) {
                    items(
                        count = pairs.size,
                        key = { it },
                        span = { GridItemSpan(1) },
                    ) { index ->
                        val (workshop, count) = pairs[index]
                        WorkshopCard(
                            workshop = workshop,
                            count = count,
                            onAvailableChange = { available ->
                                viewModel.handleAction(UpdateWorkshop(workshop, available))
                            },
                        )
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        AnimatedVisibility(isAdding) {
                            WorkshopNewTextField(
                                onSave = { viewModel.handleAction(SaveWorkshop(it)) },
                            )
                        }
                    }
                }
            } ?: LoadingBox()
        }
    }

    LoadingDialog(visible = isLoading)
}