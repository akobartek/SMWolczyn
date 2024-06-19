package pl.kapucyni.wolczyn.app.kitchen.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.usecases.GetKitchenMenuUseCase

class KitchenViewModel(private val getKitchenMenuUseCase: GetKitchenMenuUseCase) :
    BasicViewModel<KitchenMenu>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getKitchenMenuUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { menu -> _screenState.update { State.Success(menu) } }
            } catch (_: Exception) {
            }
        }
    }
}