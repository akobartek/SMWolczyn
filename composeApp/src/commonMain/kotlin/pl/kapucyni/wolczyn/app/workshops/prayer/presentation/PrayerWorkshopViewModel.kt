package pl.kapucyni.wolczyn.app.workshops.prayer.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.model.PrayerWorkshopTask
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.usecases.GetPrayerTasksUseCase

class PrayerWorkshopViewModel(
    private val getPrayerTasksUseCase: GetPrayerTasksUseCase,
) : BasicViewModel<List<PrayerWorkshopTask>>() {
    init {
        viewModelScope.launch {
            runCatching {
                getPrayerTasksUseCase().collect { tasks -> _state.update { tasks } }
            }
        }
    }
}