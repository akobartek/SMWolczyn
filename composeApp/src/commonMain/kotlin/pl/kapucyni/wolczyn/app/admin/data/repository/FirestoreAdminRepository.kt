package pl.kapucyni.wolczyn.app.admin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository
import pl.kapucyni.wolczyn.app.kitchen.data.sources.FirestoreKitchenSource

// TODO -> shop!
class FirestoreAdminRepository(
    private val kitchenSource: FirestoreKitchenSource,
) : AdminRepository<FirestoreData> {

    override fun getAppData(): Flow<FirestoreData> =
        kitchenSource.getFirestoreKitchenMenu()
            .combine(
                kitchenSource.getFirestoreKitchenPromotions(),
            ) { kitchenMenu, kitchenPromos ->
                FirestoreData(kitchenMenu, kitchenPromos)
            }
}