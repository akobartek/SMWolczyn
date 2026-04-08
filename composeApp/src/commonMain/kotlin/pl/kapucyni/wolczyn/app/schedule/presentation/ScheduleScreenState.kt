package pl.kapucyni.wolczyn.app.schedule.presentation

import androidx.compose.runtime.Immutable
import pl.kapucyni.wolczyn.app.schedule.domain.model.ScheduleDay

@Immutable
data class ScheduleScreenState(
    val schedule: List<ScheduleDay>,
    val selectedDay: Int,
    val currentDay: Int
)