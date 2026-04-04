package pl.kapucyni.wolczyn.app.admin.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.usecases.DeletePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.GetAppDataUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SaveMenuItemUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SavePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SaveQuizUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SaveShopProductUseCase
import pl.kapucyni.wolczyn.app.admin.presentation.model.AdminScreenAction
import pl.kapucyni.wolczyn.app.admin.presentation.model.AdminScreenAction.*
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.utils.randomUUID
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem
import pl.kapucyni.wolczyn.app.quiz.data.model.FirestoreQuiz
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizState
import pl.kapucyni.wolczyn.app.shop.data.model.FirestoreShopProduct

class AdminViewModel(
    private val getAppDataUseCase: GetAppDataUseCase<FirestoreData>,
    private val saveMenuItemUseCase: SaveMenuItemUseCase,
    private val saveQuizUseCase: SaveQuizUseCase,
    private val saveShopProductUseCase: SaveShopProductUseCase,
    private val savePromotionUseCase: SavePromotionUseCase,
    private val deletePromotionUseCase: DeletePromotionUseCase,
) : BasicViewModel<FirestoreData>() {

    init {
        viewModelScope.launch {
            runCatching {
                getAppDataUseCase().collect { data ->
                    _state.update { data }
                }
            }
        }
    }

    fun handleScreenAction(action: AdminScreenAction) {
        when (action) {
            is UpdateMenuItem -> onMenuItemUpdate(action.menuItem)
            is UpdateShopProduct -> onShopProductUpdate(action.shopProduct)
            is UpdateQuiz -> onQuizUpdate(action.quiz, action.newState)
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
        viewModelScope.launch(Dispatchers.Default) {
            saveShopProductUseCase(product)
        }
    }

    private fun onQuizUpdate(quiz: FirestoreQuiz, newState: QuizState) {
        viewModelScope.launch(Dispatchers.Default) {
            saveQuizUseCase(quiz.copy(state = newState))
        }
    }

    private fun onPromotionAdd(newPromotionName: String, isFromKitchen: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            savePromotionUseCase(
                FirestorePromotion(id = randomUUID(), name = newPromotionName),
                isFromKitchen
            )
        }
    }

    private fun onPromotionUpdate(promotion: FirestorePromotion, isFromKitchen: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            savePromotionUseCase(promotion, isFromKitchen)
        }
    }

    private fun onPromotionDelete(promotionId: String, isFromKitchen: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            deletePromotionUseCase(promotionId, isFromKitchen)
        }
    }
}