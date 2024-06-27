package pl.kapucyni.wolczyn.app.admin.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.usecases.DeletePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.GetAppDataUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SaveMenuItemUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SavePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SaveShopProductUseCase
import pl.kapucyni.wolczyn.app.admin.presentation.model.AdminScreenAction
import pl.kapucyni.wolczyn.app.admin.presentation.model.AdminScreenAction.*
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.utils.randomUUID
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem
import pl.kapucyni.wolczyn.app.shop.data.model.FirestoreShopProduct

class AdminViewModel(
    private val getAppDataUseCase: GetAppDataUseCase<FirestoreData>,
    private val saveMenuItemUseCase: SaveMenuItemUseCase,
    private val saveShopProductUseCase: SaveShopProductUseCase,
    private val savePromotionUseCase: SavePromotionUseCase,
    private val deletePromotionUseCase: DeletePromotionUseCase
) : BasicViewModel<FirestoreData>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getAppDataUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { data -> _screenState.update { State.Success(data) } }
            } catch (_: Exception) {
            }
        }
    }

    fun handleScreenAction(action: AdminScreenAction) {
        when (action) {
            is UpdateMenuItem -> onMenuItemUpdate(action.menuItem)
            is UpdateShopProduct -> onShopProductUpdate(action.shopProduct)
            is AddPromotion -> onPromotionAdd(action.name, action.isFromKitchen)
            is UpdatePromotion -> onPromotionUpdate(action.promotion, action.isFromKitchen)
            is DeletePromotion -> onPromotionDelete(action.id, action.isFromKitchen)
        }
    }

    private fun onMenuItemUpdate(item: FirestoreMenuItem) {
        viewModelScope.launch(Dispatchers.IO) {
            saveMenuItemUseCase(item)
        }
    }

    private fun onShopProductUpdate(product: FirestoreShopProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            saveShopProductUseCase(product)
        }
    }

    private fun onPromotionAdd(newPromotionName: String, isFromKitchen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            savePromotionUseCase(
                FirestorePromotion(id = randomUUID(), name = newPromotionName),
                isFromKitchen
            )
        }
    }

    private fun onPromotionUpdate(promotion: FirestorePromotion, isFromKitchen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            savePromotionUseCase(promotion, isFromKitchen)
        }
    }

    private fun onPromotionDelete(promotionId: String, isFromKitchen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePromotionUseCase(promotionId, isFromKitchen)
        }
    }
}