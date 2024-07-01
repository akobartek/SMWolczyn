package pl.kapucyni.wolczyn.app.shop.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.kapucyni.wolczyn.app.shop.data.sources.FirestoreShopSource
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.domain.repository.ShopRepository

class ShopRepositoryImpl(
    private val firestoreSource: FirestoreShopSource,
) : ShopRepository {
    override fun getShop(): Flow<Shop> =
        firestoreSource.getShopProducts()
            .combine(firestoreSource.getShopPromotions()) { products, promotions ->
                val availableProducts = products
                    .filter { it.isAvailable }
                    .sortedWith(compareBy({ it.importance }, { it.name }))
                    .map { it.toDomainObject() }
                Shop(
                    products = availableProducts,
                    promotions = promotions.filter { it.isValid }.map { it.name },
                )
            }
}