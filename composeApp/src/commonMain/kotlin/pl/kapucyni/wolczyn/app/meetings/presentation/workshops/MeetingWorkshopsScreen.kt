package pl.kapucyni.wolczyn.app.meetings.presentation.workshops

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.workshops_title

@Composable
fun MeetingWorkshopsScreen(
    navigateUp: () -> Unit,
    viewModel: MeetingWorkshopsViewModel,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    ScreenLayout(
        title = stringResource(Res.string.workshops_title),
        onBackPressed = navigateUp,
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
                                viewModel.updateWorkshop(workshop, available)
                            },
                        )
                    }
                }
            } ?: LoadingBox()
        }
    }
}

@Composable
private fun WorkshopCard(
    workshop: Workshop,
    count: Int,
    onAvailableChange: (Boolean) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(8.dp),
        ) {
            WolczynText(
                text = "${workshop.name} ($count)",
                textStyle = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f),
            )
            Switch(
                checked = workshop.available,
                onCheckedChange = onAvailableChange,
            )
        }
    }
}