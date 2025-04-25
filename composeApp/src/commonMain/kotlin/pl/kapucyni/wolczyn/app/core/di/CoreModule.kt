package pl.kapucyni.wolczyn.app.core.di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.core.data.repository.CoreRepositoryImpl
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetAppStateUseCase
import pl.kapucyni.wolczyn.app.core.presentation.AppViewModel
import pl.kapucyni.wolczyn.app.core.presentation.HomeScreenViewModel

val coreModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }

    single { FirestoreHomeSource() }
    single<CoreRepository> { CoreRepositoryImpl(get()) }
    single { GetAppStateUseCase(get()) }

    viewModel { AppViewModel(get()) }
    single { HomeScreenViewModel(get()) }
}