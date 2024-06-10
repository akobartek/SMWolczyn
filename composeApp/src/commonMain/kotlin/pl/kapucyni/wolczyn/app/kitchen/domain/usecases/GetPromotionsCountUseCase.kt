package pl.kapucyni.wolczyn.app.kitchen.domain.usecases

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.kitchen.domain.repository.KitchenRepository

class GetPromotionsCountUseCase(private val kitchenRepository: KitchenRepository) {
    operator fun invoke(): Flow<Int> = kitchenRepository.getPromotionsCount()
}