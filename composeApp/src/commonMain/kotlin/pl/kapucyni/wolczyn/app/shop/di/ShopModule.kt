package pl.kapucyni.wolczyn.app.shop.di

import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.shop.data.repository.ShopRepositoryImpl
import pl.kapucyni.wolczyn.app.shop.data.sources.BasicShopSource
import pl.kapucyni.wolczyn.app.shop.data.sources.FirestoreShopSource
import pl.kapucyni.wolczyn.app.shop.domain.repository.ShopRepository
import pl.kapucyni.wolczyn.app.shop.domain.usecases.GetShopUseCase
import pl.kapucyni.wolczyn.app.shop.presentation.ShopViewModel

val shopModule = module {
    single { BasicShopSource() }
    single { FirestoreShopSource() }
    single<ShopRepository> { ShopRepositoryImpl(get(), get()) }
    single { GetShopUseCase(get()) }

    factory { ShopViewModel(get()) }
}