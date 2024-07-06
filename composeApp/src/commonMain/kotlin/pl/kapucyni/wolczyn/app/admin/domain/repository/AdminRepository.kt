package pl.kapucyni.wolczyn.app.admin.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem
import pl.kapucyni.wolczyn.app.quiz.data.model.FirestoreQuiz
import pl.kapucyni.wolczyn.app.shop.data.model.FirestoreShopProduct

interface AdminRepository<out T> {
    fun getAppData(): Flow<T>
    suspend fun saveMenuItem(item: FirestoreMenuItem)
    suspend fun saveShopProduct(product: FirestoreShopProduct)
    suspend fun saveQuiz(quiz: FirestoreQuiz)
    suspend fun savePromotion(promotion: FirestorePromotion, isFromKitchen: Boolean)
    suspend fun deletePromotion(promoId: String, isFromKitchen: Boolean)
}