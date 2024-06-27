package pl.kapucyni.wolczyn.app.core.di

import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.core.data.repository.CoreRepositoryImpl
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetAppStateUseCase
import pl.kapucyni.wolczyn.app.core.presentation.HomeScreenViewModel

val coreModule = module {
    single { FirestoreHomeSource() }
    single<CoreRepository> { CoreRepositoryImpl(get()) }
    single { GetAppStateUseCase(get()) }

    single { HomeScreenViewModel(get()) }
}