package pl.kapucyni.wolczyn.app.shop.domain.model

data class ShopProduct(
    val id: String = "",
    val name: String = "",
    val photosUrls: Map<ProductColor, List<String>> = mapOf(),
    val sizes: String = "",
)
