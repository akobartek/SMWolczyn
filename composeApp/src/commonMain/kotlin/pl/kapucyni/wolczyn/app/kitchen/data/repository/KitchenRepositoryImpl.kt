package pl.kapucyni.wolczyn.app.kitchen.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pl.kapucyni.wolczyn.app.kitchen.data.sources.getBasicMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.repository.KitchenRepository

// TODO() -> Connect to firebase
class KitchenRepositoryImpl: KitchenRepository {
    override fun getKitchenMenu(): Flow<KitchenMenu> = flowOf(getBasicMenu())

    override fun getPromotionsCount(): Flow<Int> = flowOf(0)
}