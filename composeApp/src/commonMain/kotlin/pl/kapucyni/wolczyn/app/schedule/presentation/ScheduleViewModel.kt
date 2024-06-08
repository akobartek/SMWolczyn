package pl.kapucyni.wolczyn.app.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import pl.kapucyni.wolczyn.app.schedule.domain.model.ScheduleDay
import pl.kapucyni.wolczyn.app.schedule.domain.usecase.GetScheduleUseCase

class ScheduleViewModel(getScheduleUseCase: GetScheduleUseCase) : ViewModel() {

    sealed class State {
        data object Loading : State()
        data class Schedule(
            val schedule: List<ScheduleDay>,
            val selectedDay: Int,
            val currentDay: Int
        ) : State()
    }

    private val _screenState = MutableStateFlow<State>(State.Loading)
    val screenState: StateFlow<State> = _screenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getScheduleUseCase()
                    .stateIn(this, SharingStarted.WhileSubscribed(5000L), null)
                    .onEach { _screenState.update { State.Loading } }
                    .collect { schedule ->
                        schedule?.let {
                            val today = Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault()).date
                            val daysUntil = schedule.getOrNull(0)?.date?.daysUntil(today) ?: 0

                            _screenState.update {
                                State.Schedule(
                                    schedule = schedule,
                                    selectedDay = daysUntil.coerceIn(0, 4),
                                    currentDay = daysUntil
                                )
                            }
                        }
                    }
            } catch (_: Exception) {
            }
//            } catch (_: InvalidUserException) {}
        }
    }

    fun onDaySelected(day: Int) {
        _screenState.getAndUpdate { state ->
            if (state is State.Schedule)
                state.copy(selectedDay = day)
            else state
        }
    }
}