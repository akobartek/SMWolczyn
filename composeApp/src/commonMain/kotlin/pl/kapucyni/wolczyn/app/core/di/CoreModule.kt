package pl.kapucyni.wolczyn.app.core.di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.core.data.repository.CoreRepositoryImpl
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetHomeNotificationsUseCase
import AppViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import pl.kapucyni.wolczyn.app.core.presentation.HomeViewModel

val coreModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }

    singleOf(::FirestoreHomeSource)
    single<CoreRepository> { CoreRepositoryImpl(get()) }
    factoryOf(::GetHomeNotificationsUseCase)

    viewModelOf(::AppViewModel)
    viewModelOf(::HomeViewModel)
}