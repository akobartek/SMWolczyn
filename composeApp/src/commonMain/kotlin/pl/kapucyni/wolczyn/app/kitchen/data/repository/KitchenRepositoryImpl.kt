package pl.kapucyni.wolczyn.app.kitchen.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.kapucyni.wolczyn.app.kitchen.data.sources.getBasicMenu
import pl.kapucyni.wolczyn.app.kitchen.data.sources.getFirestoreKitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.data.sources.getFirestoreKitchenPromotions
import pl.kapucyni.wolczyn.app.kitchen.data.sources.getNumberOfPromotions
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.repository.KitchenRepository

class KitchenRepositoryImpl : KitchenRepository {
    override fun getKitchenMenu(): Flow<KitchenMenu> =
        getFirestoreKitchenMenu()
            .combine(getFirestoreKitchenPromotions()) { menu, promotions ->
                if (menu.isEmpty()) getBasicMenu()
                else {
                    val menuMap = menu
                        .filter { it.isAvailable }
                        .sortedWith(compareBy({ it.section.order }, { it.name }))
                        .groupBy { it.section }
                        .mapValues { (_, firestoreItems) -> firestoreItems.map { it.toDomainObject() } }
                    KitchenMenu(
                        menu = menuMap,
                        promotions = promotions.filter { it.isValid }.map { it.name },
                    )
                }
            }

    override fun getPromotionsCount(): Flow<Int> = getNumberOfPromotions()
}