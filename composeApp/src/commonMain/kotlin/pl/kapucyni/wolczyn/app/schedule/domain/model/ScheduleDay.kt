package pl.kapucyni.wolczyn.app.schedule.domain.model

import kotlinx.datetime.LocalDate

data class ScheduleDay(
    val date: LocalDate,
    val name: String,
    val events: MutableList<Event>
) {
    fun getCurrentEventIndex() = events.indexOfLast { event -> event.isEventOverOrLasting() }
}