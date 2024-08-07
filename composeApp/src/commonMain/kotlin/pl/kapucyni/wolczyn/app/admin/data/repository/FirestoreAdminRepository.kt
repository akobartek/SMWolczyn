package pl.kapucyni.wolczyn.app.admin.data.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.common.utils.combine
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem
import pl.kapucyni.wolczyn.app.kitchen.data.sources.FirestoreKitchenSource
import pl.kapucyni.wolczyn.app.quiz.data.model.FirestoreQuiz
import pl.kapucyni.wolczyn.app.quiz.data.sources.FirestoreQuizSource
import pl.kapucyni.wolczyn.app.shop.data.model.FirestoreShopProduct
import pl.kapucyni.wolczyn.app.shop.data.sources.FirestoreShopSource

class FirestoreAdminRepository(
    private val kitchenSource: FirestoreKitchenSource,
    private val shopSource: FirestoreShopSource,
    private val kitchenQuizSource: FirestoreQuizSource,
) : AdminRepository<FirestoreData> {

    override fun getAppData(): Flow<FirestoreData> =
        combine(
            kitchenSource.getKitchenMenu(),
            kitchenSource.getKitchenPromotions(),
            shopSource.getShopProducts(),
            shopSource.getShopPromotions(),
            kitchenQuizSource.getQuiz(),
            kitchenQuizSource.getResults()
        ) { kitchenMenu, kitchenPromos, shopProducts, shopPromos, quiz, quizResults ->
            FirestoreData(
                kitchenMenuItems = kitchenMenu.sortedBy { it.name },
                kitchenPromotions = kitchenPromos.sortedBy { it.name },
                kitchenQuiz = quiz,
                kitchenQuizResults =
                quizResults.sortedWith(compareBy({ it.score * -1 }, { it.submittedAt })),
                shopProducts = shopProducts.sortedBy { it.name },
                shopPromotions = shopPromos.sortedBy { it.name },
            )
        }

    override suspend fun saveMenuItem(item: FirestoreMenuItem) =
        kitchenSource.saveMenuItem(item)

    override suspend fun saveShopProduct(product: FirestoreShopProduct) =
        shopSource.saveShopProduct(product)

    override suspend fun saveQuiz(quiz: FirestoreQuiz) =
        kitchenQuizSource.saveQuiz(quiz)

    override suspend fun savePromotion(promotion: FirestorePromotion, isFromKitchen: Boolean) =
        if (isFromKitchen) kitchenSource.savePromotion(promotion)
        else shopSource.savePromotion(promotion)

    override suspend fun deletePromotion(promoId: String, isFromKitchen: Boolean) =
        if (isFromKitchen) kitchenSource.deletePromotion(promoId)
        else shopSource.deletePromotion(promoId)
}