package pl.kapucyni.wolczyn.app.core.di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.core.data.repository.CoreRepositoryImpl
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetHomeNotificationsUseCase
import AppViewModel
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetAppConfigurationUseCase
import pl.kapucyni.wolczyn.app.core.presentation.HomeViewModel

val coreModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }

    single { FirestoreHomeSource() }
    single<CoreRepository> { CoreRepositoryImpl(get()) }
    factory { GetHomeNotificationsUseCase(get()) }
    factory { GetAppConfigurationUseCase(get()) }

    viewModel { AppViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
}