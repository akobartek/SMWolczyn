package pl.kapucyni.wolczyn.app.core.presentation

import androidx.lifecycle.viewModelScope
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
        _state.update { emptyList() }
        viewModelScope.launch {
            try {
                getHomeNotificationsUseCase().collect { homeInfo ->
                    _state.update { homeInfo }
                }
            } catch (_: Exception) {
            }
        }
    }
}