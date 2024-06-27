package pl.kapucyni.wolczyn.app.admin.presentation.model

import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.kitchen.data.model.FirestoreMenuItem
import pl.kapucyni.wolczyn.app.shop.data.model.FirestoreShopProduct

data class AdminData(
    val id: String,
    val name: String,
    val isChecked: Boolean
) {
    companion object {
        fun fromPromotion(promotion: FirestorePromotion) =
            AdminData(promotion.id, promotion.name, promotion.isValid)

        fun fromMenuItem(menuItem: FirestoreMenuItem) =
            AdminData(menuItem.id, menuItem.name, menuItem.isAvailable)

        fun fromShopProduct(product: FirestoreShopProduct) =
            AdminData(product.id, product.name, product.isAvailable)
    }
}