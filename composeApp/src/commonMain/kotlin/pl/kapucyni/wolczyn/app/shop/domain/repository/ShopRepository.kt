package pl.kapucyni.wolczyn.app.shop.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop

interface ShopRepository {
    fun getShop(): Flow<Shop>
}