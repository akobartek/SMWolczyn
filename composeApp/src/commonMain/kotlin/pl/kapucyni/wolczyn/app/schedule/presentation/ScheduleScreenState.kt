package pl.kapucyni.wolczyn.app.schedule.presentation

import pl.kapucyni.wolczyn.app.schedule.domain.model.ScheduleDay

data class ScheduleScreenState(
    val schedule: List<ScheduleDay>,
    val selectedDay: Int,
    val currentDay: Int
)