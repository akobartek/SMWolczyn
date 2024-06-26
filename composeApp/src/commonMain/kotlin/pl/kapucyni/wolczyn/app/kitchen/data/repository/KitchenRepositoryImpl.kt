package pl.kapucyni.wolczyn.app.kitchen.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.kapucyni.wolczyn.app.kitchen.data.sources.BasicKitchenSource
import pl.kapucyni.wolczyn.app.kitchen.data.sources.FirestoreKitchenSource
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.repository.KitchenRepository

class KitchenRepositoryImpl(
    private val firestoreSource: FirestoreKitchenSource,
    private val basicSource: BasicKitchenSource,
) : KitchenRepository {
    override fun getKitchenMenu(): Flow<KitchenMenu> =
        firestoreSource.getKitchenMenu()
            .combine(firestoreSource.getKitchenPromotions()) { menu, promotions ->
                if (menu.isEmpty()) basicSource.getBasicMenu()
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
}