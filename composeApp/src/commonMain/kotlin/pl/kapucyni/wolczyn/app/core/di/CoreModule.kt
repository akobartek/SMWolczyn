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
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import pl.kapucyni.wolczyn.app.core.data.repository.CrashlyticsLogRepository
import pl.kapucyni.wolczyn.app.core.domain.repository.LogRepository
import pl.kapucyni.wolczyn.app.core.presentation.HomeViewModel
import pl.kapucyni.wolczyn.app.core.presentation.UserPreferencesRepository

val coreModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    single { Firebase.crashlytics }
    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }

    singleOf(::CrashlyticsLogRepository) bind LogRepository::class
    singleOf(::UserPreferencesRepository)
    singleOf(::FirestoreHomeSource)
    single<CoreRepository> { CoreRepositoryImpl(get()) }
    factoryOf(::GetHomeNotificationsUseCase)

    viewModelOf(::AppViewModel)
    viewModelOf(::HomeViewModel)
}