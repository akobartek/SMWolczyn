package pl.kapucyni.wolczyn.app.shop.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pl.kapucyni.wolczyn.app.shop.data.sources.basicShop
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.domain.repository.ShopRepository

// TODO() -> Connect to firebase
class ShopRepositoryImpl: ShopRepository {
    override fun getShop(): Flow<Shop> = flowOf(basicShop)
}