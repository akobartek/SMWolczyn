package pl.kapucyni.wolczyn.app.shop.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.shop.data.repository.ShopRepositoryImpl
import pl.kapucyni.wolczyn.app.shop.data.sources.FirestoreShopSource
import pl.kapucyni.wolczyn.app.shop.domain.repository.ShopRepository
import pl.kapucyni.wolczyn.app.shop.domain.usecases.GetShopUseCase
import pl.kapucyni.wolczyn.app.shop.presentation.ShopViewModel

val shopModule = module {
    single { FirestoreShopSource() }
    single<ShopRepository> { ShopRepositoryImpl(get()) }
    single { GetShopUseCase(get()) }

    viewModel { ShopViewModel(get()) }
}