package pl.kapucyni.wolczyn.app.schedule.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.schedule.domain.usecases.GetScheduleUseCase

class ScheduleViewModel(private val getScheduleUseCase: GetScheduleUseCase) :
    BasicViewModel<ScheduleScreenState>() {

    init {
        viewModelScope.launch {
            runCatching {
                getScheduleUseCase().collect { schedule ->
                    val today = Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date
                    val daysUntil = schedule.getOrNull(0)?.date?.daysUntil(today) ?: 0

                    _state.update {
                        ScheduleScreenState(
                            schedule = schedule,
                            selectedDay = daysUntil.coerceIn(0, 4),
                            currentDay = daysUntil,
                        )
                    }
                }
            }
        }
    }

    fun onDaySelected(day: Int) {
        _state.update { it?.copy(selectedDay = day) }
    }
}