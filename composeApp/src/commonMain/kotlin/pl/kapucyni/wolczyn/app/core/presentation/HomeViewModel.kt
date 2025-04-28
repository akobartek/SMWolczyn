package pl.kapucyni.wolczyn.app.core.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.core.domain.model.AppState
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetAppStateUseCase

class HomeViewModel(
    private val getAppStateUseCase: GetAppStateUseCase,
) : BasicViewModel<AppState>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getAppStateUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { homeInfo -> _screenState.update { State.Success(homeInfo) } }
            } catch (_: Exception) {
            }
        }
    }
}