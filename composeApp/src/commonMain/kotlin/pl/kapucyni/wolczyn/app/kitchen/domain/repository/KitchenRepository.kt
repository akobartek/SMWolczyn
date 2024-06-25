package pl.kapucyni.wolczyn.app.kitchen.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu

interface KitchenRepository {
    fun getKitchenMenu(): Flow<KitchenMenu>
}