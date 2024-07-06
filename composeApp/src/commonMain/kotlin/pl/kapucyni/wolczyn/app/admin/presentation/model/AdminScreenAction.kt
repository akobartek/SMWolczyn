package pl.kapucyni.wolczyn.app.admin.presentation.model

import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem
import pl.kapucyni.wolczyn.app.quiz.data.model.FirestoreQuiz
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizState
import pl.kapucyni.wolczyn.app.shop.data.model.FirestoreShopProduct

sealed class AdminScreenAction {
    data class UpdateMenuItem(val menuItem: FirestoreMenuItem) : AdminScreenAction()

    data class UpdateShopProduct(val shopProduct: FirestoreShopProduct) : AdminScreenAction()

    data class UpdateQuiz(val quiz: FirestoreQuiz, val newState: QuizState) : AdminScreenAction()

    data class AddPromotion(val name: String, val isFromKitchen: Boolean) : AdminScreenAction()

    data class UpdatePromotion(val promotion: FirestorePromotion, val isFromKitchen: Boolean) :
        AdminScreenAction()

    data class DeletePromotion(val id: String, val isFromKitchen: Boolean) : AdminScreenAction()
}