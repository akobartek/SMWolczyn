package pl.kapucyni.wolczyn.app.kitchen.di

import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.kitchen.data.repository.KitchenRepositoryImpl
import pl.kapucyni.wolczyn.app.kitchen.data.sources.BasicKitchenSource
import pl.kapucyni.wolczyn.app.kitchen.data.sources.FirestoreKitchenSource
import pl.kapucyni.wolczyn.app.kitchen.domain.repository.KitchenRepository
import pl.kapucyni.wolczyn.app.kitchen.domain.usecases.GetKitchenMenuUseCase
import pl.kapucyni.wolczyn.app.kitchen.presentation.KitchenViewModel

val kitchenModule = module {
    single { FirestoreKitchenSource() }
    single { BasicKitchenSource() }
    single<KitchenRepository> { KitchenRepositoryImpl(get(), get()) }
    single { GetKitchenMenuUseCase(get()) }

    factory { KitchenViewModel(get()) }
}