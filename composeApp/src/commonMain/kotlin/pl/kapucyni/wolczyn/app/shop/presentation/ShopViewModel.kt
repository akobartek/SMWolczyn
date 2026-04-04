package pl.kapucyni.wolczyn.app.shop.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.domain.usecases.GetShopUseCase

class ShopViewModel(private val getShopUseCase: GetShopUseCase) : BasicViewModel<Shop>() {

    init {
        viewModelScope.launch {
            runCatching {
                getShopUseCase().collect { shop ->
                    _state.update { shop }
                }
            }
        }
    }
}