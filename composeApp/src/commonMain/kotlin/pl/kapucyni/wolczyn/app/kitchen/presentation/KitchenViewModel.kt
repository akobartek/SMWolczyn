package pl.kapucyni.wolczyn.app.kitchen.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                    .collect { menu ->
                        _screenState.update { State.Success(menu) }
                        _openPromotions.update { menu.promotions }
                    }
            } catch (_: Exception) {
            }
        }
    }

    private val _openPromotions = MutableStateFlow<List<String>>(listOf())
    val openPromotions: StateFlow<List<String>> = _openPromotions.asStateFlow()

    fun removePromotion(name: String) {
        _openPromotions.update { currentList -> currentList.filter { it != name }}
    }
}