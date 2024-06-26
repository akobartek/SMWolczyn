package pl.kapucyni.wolczyn.app.admin.di

import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.data.repository.FirestoreAdminRepository
import pl.kapucyni.wolczyn.app.admin.domain.repository.AdminRepository
import pl.kapucyni.wolczyn.app.admin.domain.usecases.DeletePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.GetAppDataUseCase
import pl.kapucyni.wolczyn.app.admin.domain.usecases.SavePromotionUseCase
import pl.kapucyni.wolczyn.app.admin.presentation.AdminViewModel

val adminModule = module {
    single<AdminRepository<FirestoreData>> { FirestoreAdminRepository(get(), get()) }
    single { GetAppDataUseCase<FirestoreData>(get()) }
    single { SavePromotionUseCase(get()) }
    single { DeletePromotionUseCase(get()) }

    factory { AdminViewModel(get(), get(), get()) }
}