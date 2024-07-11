package pl.kapucyni.wolczyn.app.workshops.prayer.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.model.PrayerWorkshopTask
import pl.kapucyni.wolczyn.app.workshops.prayer.domain.usecases.GetPrayerTasksUseCase

class PrayerWorkshopViewModel(
    private val getPrayerTasksUseCase: GetPrayerTasksUseCase,
) : BasicViewModel<List<PrayerWorkshopTask>>() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getPrayerTasksUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { tasks -> _screenState.update { State.Success(tasks) } }
            } catch (_: Exception) {
            }
        }
    }
}