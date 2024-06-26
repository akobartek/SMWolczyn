package pl.kapucyni.wolczyn.app.admin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.kitchen.data.sources.FirestoreKitchenSource
import pl.kapucyni.wolczyn.app.shop.data.sources.FirestoreShopSource

class FirestoreAdminRepository(
    private val kitchenSource: FirestoreKitchenSource,
    private val shopSource: FirestoreShopSource,
) : AdminRepository<FirestoreData> {

    override fun getAppData(): Flow<FirestoreData> =
        combine(
            kitchenSource.getKitchenMenu(),
            kitchenSource.getKitchenPromotions(),
            shopSource.getShopProducts(),
            shopSource.getShopPromotions(),
        ) { kitchenMenu, kitchenPromos, shopProducts, shopPromos ->
            FirestoreData(
                kitchenMenu.sortedBy { it.name },
                kitchenPromos.sortedBy { it.name },
                shopProducts.sortedBy { it.name },
                shopPromos.sortedBy { it.name },
            )
        }

    override suspend fun savePromotion(promotion: FirestorePromotion, isFromKitchen: Boolean) =
        if (isFromKitchen) kitchenSource.savePromotion(promotion)
        else shopSource.savePromotion(promotion)

    override suspend fun deletePromotion(promoId: String, isFromKitchen: Boolean) =
        if (isFromKitchen) kitchenSource.deletePromotion(promoId)
        else shopSource.deletePromotion(promoId)
}