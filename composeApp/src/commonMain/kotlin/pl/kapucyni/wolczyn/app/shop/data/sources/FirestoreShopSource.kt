package pl.kapucyni.wolczyn.app.shop.data.sources

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.common.utils.deleteObject
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollection
import pl.kapucyni.wolczyn.app.common.utils.saveObject
import pl.kapucyni.wolczyn.app.shop.data.model.FirestoreShopProduct

class FirestoreShopSource {
    companion object {
        private const val SHOP_PRODUCTS_COLLECTION = "shop_products"
        private const val SHOP_PROMOTIONS_COLLECTION = "shop_promotions"
    }

    fun getShopProducts(): Flow<List<FirestoreShopProduct>> =
        Firebase.firestore.getFirestoreCollection(SHOP_PRODUCTS_COLLECTION)

    fun getShopPromotions(): Flow<List<FirestorePromotion>> =
        Firebase.firestore.getFirestoreCollection(SHOP_PROMOTIONS_COLLECTION)

    suspend fun saveShopProduct(product: FirestoreShopProduct) =
        Firebase.firestore.saveObject(
            collectionName = SHOP_PROMOTIONS_COLLECTION,
            id = product.id,
            data = product
        )

    suspend fun savePromotion(promotion: FirestorePromotion) =
        Firebase.firestore.saveObject(
            collectionName = SHOP_PROMOTIONS_COLLECTION,
            id = promotion.id,
            data = promotion
        )

    suspend fun deletePromotion(promoId: String) =
        Firebase.firestore.deleteObject(
            collectionName = SHOP_PROMOTIONS_COLLECTION,
            id = promoId
        )
}