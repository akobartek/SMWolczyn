package pl.kapucyni.wolczyn.app.kitchen.data.sources

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollection
import pl.kapucyni.wolczyn.app.common.utils.getFirestoreCollectionCount
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuPromotion

private const val KITCHEN_MENU_COLLECTION = "kitchen_menu"
private const val KITCHEN_PROMOTIONS_COLLECTION = "kitchen_promotions"

internal fun getFirestoreKitchenMenu(): Flow<List<FirestoreMenuItem>> =
    Firebase.firestore.getFirestoreCollection(KITCHEN_MENU_COLLECTION)

internal fun getFirestoreKitchenPromotions(): Flow<List<FirestoreMenuPromotion>> =
    Firebase.firestore.getFirestoreCollection(KITCHEN_PROMOTIONS_COLLECTION)

internal fun getNumberOfPromotions(): Flow<Int> =
    Firebase.firestore.getFirestoreCollectionCount(KITCHEN_PROMOTIONS_COLLECTION)
