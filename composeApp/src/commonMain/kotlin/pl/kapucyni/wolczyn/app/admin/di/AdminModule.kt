package pl.kapucyni.wolczyn.app.admin.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.data.repository.FirestoreAdminRepository
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository
import pl.kapucyni.wolczyn.app.admin.domain.usecases.DeletePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.GetAppDataUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SaveMenuItemUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SavePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SaveQuizUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SaveShopProductUseCase
import pl.kapucyni.wolczyn.app.admin.presentation.AdminViewModel
import pl.kapucyni.wolczyn.app.kitchen.di.KITCHEN_QUIZ_SOURCE

val adminModule = module {
    single<AdminRepository<FirestoreData>> {
        FirestoreAdminRepository(get(), get(), get(qualifier = named(KITCHEN_QUIZ_SOURCE)))
    }
    single { GetAppDataUseCase<FirestoreData>(get()) }
    single { SaveMenuItemUseCase(get()) }
    single { SaveQuizUseCase(get()) }
    single { SaveShopProductUseCase(get()) }
    single { SavePromotionUseCase(get()) }
    single { DeletePromotionUseCase(get()) }

    viewModel { AdminViewModel(get(), get(), get(), get(), get(), get()) }
}