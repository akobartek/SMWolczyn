package pl.kapucyni.wolczyn.app.admin.data.model

import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuPromotion

data class FirestoreData(
    val kitchenMenuItems: List<FirestoreMenuItem>,
    val kitchenPromotions: List<FirestoreMenuPromotion>,
    // TODO -> quiz and shop
)