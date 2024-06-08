package pl.kapucyni.wolczyn.app.schedule.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.schedule.presentation.composables.EventCard
import pl.kapucyni.wolczyn.app.schedule.presentation.composables.ScheduleDaySelector
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.schedule_days
import smwolczyn.composeapp.generated.resources.schedule_title

@Composable
fun ScheduleScreen(
    onBackPressed: () -> Unit,
    navigateTo: (HomeTileType) -> Unit,
    viewModel: ScheduleViewModel = koinInject()
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()

    ScreenLayout(
        title = stringResource(Res.string.schedule_title),
        onBackPressed = onBackPressed
    ) {
        ScheduleScreenContent(
            screenState = screenState,
            onDaySelected = viewModel::onDaySelected,
            navigateTo = navigateTo
        )
    }
}

@Composable
fun ScheduleScreenContent(
    screenState: ScheduleViewModel.State,
    onDaySelected: (Int) -> Unit,
    navigateTo: (HomeTileType) -> Unit
) {
    if (screenState is ScheduleViewModel.State.Loading)
        LoadingBox()
    else {
        val state = screenState as ScheduleViewModel.State.Schedule
        val lazyListState = rememberLazyListState()
        var currentEventIndex by remember { mutableIntStateOf(-1) }

        LaunchedEffect(state.selectedDay) {
            val index =
                if (state.selectedDay == state.currentDay) {
                    currentEventIndex =
                        state.schedule.getOrNull(state.selectedDay)?.getCurrentEventIndex() ?: -1
                    currentEventIndex
                } else 0
            lazyListState.animateScrollToItem(index.coerceAtLeast(0))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .height(104.dp)
                .padding(horizontal = 16.dp)
        ) {
            state.schedule.forEachIndexed { index, scheduleDay ->
                ScheduleDaySelector(
                    day = scheduleDay.date.dayOfMonth,
                    name = scheduleDay.name,
                    isSelected = state.selectedDay == index,
                    onClick = { onDaySelected(index) }
                )
            }
        }

        state.schedule.getOrNull(state.selectedDay)?.let { scheduleDay ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        WolczynText(
                            text = stringArrayResource(Res.array.schedule_days)[state.selectedDay].uppercase(),
                            textStyle = TextStyle(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        WolczynText(
                            text = scheduleDay.name,
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                letterSpacing = (0.5).sp
                            )
                        )
                    }
                }

                items(items = scheduleDay.events, key = { it.id }) { event ->
                    EventCard(
                        event = event,
                        filledCircle = currentEventIndex >= scheduleDay.events.indexOf(event),
                        isLast = scheduleDay.events.indexOf(event) == scheduleDay.events.lastIndex,
                        onIconClick = navigateTo
                    )
                }
            }
        }
    }
}