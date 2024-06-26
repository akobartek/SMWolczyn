package pl.kapucyni.wolczyn.app.admin.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion

interface AdminRepository<out T> {
    fun getAppData(): Flow<T>
    suspend fun savePromotion(promotion: FirestorePromotion, isFromKitchen: Boolean)
    suspend fun deletePromotion(promoId: String, isFromKitchen: Boolean)
}