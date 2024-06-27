package pl.kapucyni.wolczyn.app.schedule.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.schedule.domain.usecases.GetScheduleUseCase

class ScheduleViewModel(private val getScheduleUseCase: GetScheduleUseCase) :
    BasicViewModel<ScheduleScreenState>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getScheduleUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { schedule ->
                        val today = Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date
                        val daysUntil = schedule.getOrNull(0)?.date?.daysUntil(today) ?: 0

                        _screenState.update {
                            State.Success(
                                ScheduleScreenState(
                                    schedule = schedule,
                                    selectedDay = daysUntil.coerceIn(0, 4),
                                    currentDay = daysUntil
                                )
                            )
                        }
                    }
            } catch (_: Exception) {
            }
        }
    }

    fun onDaySelected(day: Int) {
        _screenState.getAndUpdate { state ->
            when (state) {
                is State.Success -> {
                    State.Success(data = state.data.copy(selectedDay = day))
                }

                else -> state
            }
        }
    }
}