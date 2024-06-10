package pl.kapucyni.wolczyn.app.shop.domain.model

data class Shop(
    val products: List<ShopProduct> = listOf(),
    val promotions: List<String> = listOf()
)
