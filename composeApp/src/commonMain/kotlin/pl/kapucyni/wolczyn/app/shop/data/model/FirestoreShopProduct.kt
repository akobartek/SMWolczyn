package pl.kapucyni.wolczyn.app.shop.data.model

import kotlinx.serialization.Serializable
import pl.kapucyni.wolczyn.app.shop.domain.model.ProductColor
import pl.kapucyni.wolczyn.app.shop.domain.model.ShopProduct

@Serializable
data class FirestoreShopProduct(
    val id: String = "",
    val name: String = "",
    val photoUrls: Map<ProductColor, List<String>> = mapOf(),
    val sizes: String = "",
    val isAvailable: Boolean = true,
    val importance: Int = 0,
) {
    fun toDomainObject() = ShopProduct(
        id = id,
        name = name,
        photoUrls = photoUrls,
        sizes = sizes,
    )
}