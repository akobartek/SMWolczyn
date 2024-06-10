package pl.kapucyni.wolczyn.app.kitchen.domain.usecases

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.repository.KitchenRepository

class GetKitchenMenuUseCase(private val kitchenRepository: KitchenRepository) {
    operator fun invoke(): Flow<KitchenMenu> = kitchenRepository.getKitchenMenu()
}