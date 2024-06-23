package pl.kapucyni.wolczyn.app.kitchen.di

import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.kitchen.data.repository.KitchenRepositoryImpl
import pl.kapucyni.wolczyn.app.kitchen.domain.repository.KitchenRepository
import pl.kapucyni.wolczyn.app.kitchen.domain.usecases.GetKitchenMenuUseCase
import pl.kapucyni.wolczyn.app.kitchen.domain.usecases.GetPromotionsCountUseCase
import pl.kapucyni.wolczyn.app.kitchen.presentation.KitchenViewModel

val kitchenModule = module {
    single<KitchenRepository> { KitchenRepositoryImpl() }
    single { GetKitchenMenuUseCase(get()) }
    single { GetPromotionsCountUseCase(get()) }

    factory { KitchenViewModel(get()) }
}