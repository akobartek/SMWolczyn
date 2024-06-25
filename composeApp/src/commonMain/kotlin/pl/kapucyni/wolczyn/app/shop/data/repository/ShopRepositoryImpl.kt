package pl.kapucyni.wolczyn.app.shop.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pl.kapucyni.wolczyn.app.shop.data.sources.BasicShopSource
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.domain.repository.ShopRepository

// TODO() -> Connect to firebase
class ShopRepositoryImpl(
//    private val firestoreSource:,
    private val basicSource: BasicShopSource
): ShopRepository {
    override fun getShop(): Flow<Shop> = flowOf(basicSource.getBasicShop())
}