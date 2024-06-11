package pl.kapucyni.wolczyn.app.shop.domain.model

data class ShopProduct(
    val id: String = "",
    val name: String = "",
    val photosUrls: Map<ShopColor, List<String>> = mapOf(),
    val sizes: String = "",
    val available: Boolean = true,
)
