package pl.kapucyni.wolczyn.app.shop.data.sources

import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.domain.model.ShopColor.*
import pl.kapucyni.wolczyn.app.shop.domain.model.ShopProduct

internal val basicShop = Shop(
    products = listOf(
        ShopProduct(
            id = "1",
            name = "koszulka 30-lecie SMW",
            photosUrls = mapOf(
                BLACK to listOf(),
                BROWN to listOf(),
                WHITE to listOf(),
            ),
            sizes = "XS, S, M, L, XL",
        ),
        ShopProduct(
            id = "2",
            name = "koszulka \"jest radość\"",
            photosUrls = mapOf(
                BLACK to listOf(),
                BROWN to listOf(),
                WHITE to listOf(),
            ),
            sizes = "XS, S, M, L, XL",
        ),
        ShopProduct(
            id = "3",
            name = "koszulka \"sól ziemi\"",
            photosUrls = mapOf(
                BLACK to listOf(),
                BROWN to listOf(),
                WHITE to listOf(),
            ),
            sizes = "XS, S, M, L, XL",
        ),
        ShopProduct(
            id = "4",
            name = "koszulka z logiem SMW",
            photosUrls = mapOf(
                BLACK to listOf(),
                BROWN to listOf(),
                WHITE to listOf(),
            ),
            sizes = "XS, S, M, L, XL",
        ),
    ),
)