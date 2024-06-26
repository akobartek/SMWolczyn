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
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SavePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.presentation.AdminScreenAction.*
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.utils.randomUUID

class AdminViewModel(
    private val getAppDataUseCase: GetAppDataUseCase<FirestoreData>,
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
            is AddPromotion -> onPromotionAdd(action.name, action.isFromKitchen)
            is UpdatePromotion -> onPromotionUpdate(action.promotion, action.isFromKitchen)
            is DeletePromotion -> onPromotionDelete(action.id, action.isFromKitchen)
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