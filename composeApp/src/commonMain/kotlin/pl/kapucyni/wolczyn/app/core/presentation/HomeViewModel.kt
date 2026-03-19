package pl.kapucyni.wolczyn.app.core.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.core.domain.model.HomeNotification
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetHomeNotificationsUseCase

class HomeViewModel(
    coreRepository: CoreRepository,
    private val getHomeNotificationsUseCase: GetHomeNotificationsUseCase,
) : BasicViewModel<List<HomeNotification>>() {

    val appConfiguration = coreRepository.appConfiguration

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getHomeNotificationsUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { homeInfo -> _screenState.update { State.Success(homeInfo) } }
            } catch (_: Exception) {
            }
        }
    }
}