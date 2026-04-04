package pl.kapucyni.wolczyn.app.kitchen.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.usecases.GetKitchenMenuUseCase

class KitchenViewModel(private val getKitchenMenuUseCase: GetKitchenMenuUseCase) :
    BasicViewModel<KitchenMenu>() {

    private val _openPromotions = MutableStateFlow<List<String>>(listOf())
    val openPromotions: StateFlow<List<String>> = _openPromotions.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                getKitchenMenuUseCase().collect { menu ->
                    _state.update { menu }
                    _openPromotions.update { menu.promotions }
                }
            }
        }
    }

    fun removePromotion(name: String) {
        _openPromotions.update { currentList -> currentList.filter { it != name } }
    }
}