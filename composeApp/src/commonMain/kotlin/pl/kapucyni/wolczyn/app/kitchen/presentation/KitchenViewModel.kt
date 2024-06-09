package pl.kapucyni.wolczyn.app.kitchen.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.BasicViewModel
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.usecase.GetKitchenMenuUseCase

class KitchenViewModel(private val getKitchenMenuUseCase: GetKitchenMenuUseCase) :
    BasicViewModel<KitchenMenu>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getKitchenMenuUseCase()
                    .stateIn(this, SharingStarted.WhileSubscribed(5000L), null)
                    .onEach { _screenState.update { State.Loading } }
                    .collect { menu ->
                        menu?.let { _screenState.update { State.Success(menu) } }
                    }
            } catch (_: Exception) {
            }
        }
    }
}