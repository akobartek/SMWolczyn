package pl.kapucyni.wolczyn.app.shop.data.sources

import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.domain.model.ProductColor.*
import pl.kapucyni.wolczyn.app.shop.domain.model.ShopProduct

class BasicShopSource {
    fun getBasicShop() = Shop(
        products = listOf(
            ShopProduct(
                id = "1",
                name = "koszulka 30-lecie SMW",
                photosUrls = mapOf(
                    NONE to listOf(),
                    BLACK to listOf(),
                    WHITE to listOf(),
                ),
                sizes = "XS, S, M, L, XL",
            ),
            ShopProduct(
                id = "2",
                name = "koszulka \"jest radość\"",
                photosUrls = mapOf(
                    NONE to listOf(),
                    BLACK to listOf(),
                    BROWN to listOf(),
                    GREEN to listOf(),
                    WHITE to listOf(),
                ),
                sizes = "XS, S, M, L, XL",
            ),
        ),
    )
}