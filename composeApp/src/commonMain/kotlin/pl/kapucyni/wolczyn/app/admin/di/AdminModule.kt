package pl.kapucyni.wolczyn.app.admin.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.data.repository.FirestoreAdminRepository
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository
import pl.kapucyni.wolczyn.app.admin.domain.usecases.DeletePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.GetAppDataUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SaveMenuItemUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SavePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SaveShopProductUseCase
import pl.kapucyni.wolczyn.app.admin.presentation.AdminViewModel

val adminModule = module {
    single<AdminRepository<FirestoreData>> { FirestoreAdminRepository(get(), get()) }
    single { GetAppDataUseCase<FirestoreData>(get()) }
    single { SaveMenuItemUseCase(get()) }
    single { SaveShopProductUseCase(get()) }
    single { SavePromotionUseCase(get()) }
    single { DeletePromotionUseCase(get()) }

    viewModel { AdminViewModel(get(), get(), get(), get(), get()) }
}