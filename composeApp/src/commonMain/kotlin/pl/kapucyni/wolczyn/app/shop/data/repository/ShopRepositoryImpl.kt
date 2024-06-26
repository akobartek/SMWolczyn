package pl.kapucyni.wolczyn.app.shop.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.kapucyni.wolczyn.app.shop.data.sources.BasicShopSource
import pl.kapucyni.wolczyn.app.shop.data.sources.FirestoreShopSource
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.domain.repository.ShopRepository

class ShopRepositoryImpl(
    private val firestoreSource: FirestoreShopSource,
    private val basicSource: BasicShopSource
) : ShopRepository {
    override fun getShop(): Flow<Shop> =
        firestoreSource.getShopProducts()
            .combine(firestoreSource.getShopPromotions()) { products, promotions ->
                // TODO
                basicSource.getBasicShop()
            }
}