package pl.kapucyni.wolczyn.app.shop.domain.usecases

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.domain.repository.ShopRepository

class GetShopUseCase(private val shopRepository: ShopRepository) {
    operator fun invoke(): Flow<Shop> = shopRepository.getShop()
}