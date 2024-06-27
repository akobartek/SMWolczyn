package pl.kapucyni.wolczyn.app.kitchen.data.sources

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.common.utils.deleteObject
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollection
import pl.kapucyni.wolczyn.app.common.utils.saveObject
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem

class FirestoreKitchenSource {

    companion object {
        private const val KITCHEN_MENU_COLLECTION = "kitchen_menu"
        private const val KITCHEN_PROMOTIONS_COLLECTION = "kitchen_promotions"
    }

    fun getKitchenMenu(): Flow<List<FirestoreMenuItem>> =
        Firebase.firestore.getFirestoreCollection(KITCHEN_MENU_COLLECTION)

    fun getKitchenPromotions(): Flow<List<FirestorePromotion>> =
        Firebase.firestore.getFirestoreCollection(KITCHEN_PROMOTIONS_COLLECTION)

    suspend fun saveMenuItem(item: FirestoreMenuItem) =
        Firebase.firestore.saveObject(
            collectionName = KITCHEN_MENU_COLLECTION,
            id = item.id,
            data = item
        )

    suspend fun savePromotion(promotion: FirestorePromotion) =
        Firebase.firestore.saveObject(
            collectionName = KITCHEN_PROMOTIONS_COLLECTION,
            id = promotion.id,
            data = promotion
        )

    suspend fun deletePromotion(promoId: String) =
        Firebase.firestore.deleteObject(
            collectionName = KITCHEN_PROMOTIONS_COLLECTION,
            id = promoId
        )
}


