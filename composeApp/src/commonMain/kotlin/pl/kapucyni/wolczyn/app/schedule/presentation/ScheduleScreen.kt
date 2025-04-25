package pl.kapucyni.wolczyn.app.schedule.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.schedule.presentation.composables.EventCard
import pl.kapucyni.wolczyn.app.schedule.presentation.composables.ScheduleDaySelector
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.schedule_days
import smwolczyn.composeapp.generated.resources.schedule_title

@Composable
fun ScheduleScreen(
    onBackPressed: () -> Unit,
    navigateTo: (Screen) -> Unit,
    viewModel: ScheduleViewModel = koinInject(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    ScreenLayout(
        title = stringResource(Res.string.schedule_title),
        onBackPressed = onBackPressed,
    ) {
        ScheduleScreenContent(
            screenState = screenState,
            onDaySelected = viewModel::onDaySelected,
            navigateTo = navigateTo,
        )
    }
}

@Composable
fun ScheduleScreenContent(
    screenState: State<ScheduleScreenState>,
    onDaySelected: (Int) -> Unit,
    navigateTo: (Screen) -> Unit,
) {
    when (screenState) {
        is State.Loading -> LoadingBox()
        is State.Success -> {
            val state = screenState.data
            val lazyListState = rememberLazyListState()
            var currentEventIndex by remember { mutableIntStateOf(-1) }

            LaunchedEffect(state.selectedDay) {
                currentEventIndex =
                    if (state.selectedDay == state.currentDay)
                        state.schedule.getOrNull(state.selectedDay)?.getCurrentEventIndex() ?: -1
                    else if (state.selectedDay < state.currentDay)
                        state.schedule.getOrNull(state.selectedDay)?.events?.lastIndex ?: -1
                    else -1
                val index =
                    if (state.selectedDay == state.currentDay) currentEventIndex
                    else 0
                lazyListState.animateScrollToItem(index.coerceAtLeast(0))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .height(104.dp)
                    .padding(horizontal = 6.dp)
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
                                textStyle = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                ),
                            )
                            WidthSpacer(4.dp)
                            WolczynText(
                                text = scheduleDay.name.replace("\n", ""),
                                textStyle = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.outline,
                                    letterSpacing = (0.5).sp,
                                )
                            )
                        }
                    }

                    itemsIndexed(
                        items = scheduleDay.events,
                        key = { _, event -> event.id }
                    ) { index, event ->
                        EventCard(
                            event = event,
                            filledCircle = currentEventIndex >= index,
                            hideTime =
                            if (index > 0) scheduleDay.events[index - 1].time == event.time
                            else false,
                            isLast = index == scheduleDay.events.lastIndex,
                            onNavClick = navigateTo,
                        )
                    }
                }
            }
        }
    }
}