package pl.kapucyni.wolczyn.app.shop.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.domain.usecases.GetShopUseCase

class ShopViewModel(private val getShopUseCase: GetShopUseCase): BasicViewModel<Shop>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getShopUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { shop -> _screenState.update { State.Success(shop) } }
            } catch (_: Exception) {
            }
        }
    }
}