package pl.kapucyni.wolczyn.app.core.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.core.data.repository.CoreRepositoryImpl
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.data.sources.WolczynApi
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetAppStateUseCase
import pl.kapucyni.wolczyn.app.core.domain.usecases.LoginUseCase
import pl.kapucyni.wolczyn.app.core.presentation.HomeScreenViewModel

val coreModule = module {
    single {
        HttpClient {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            defaultRequest {
                url("https://api.kapucyni.pl/")
            }
        }
    }

    single { FirestoreHomeSource() }
    single { WolczynApi(get()) }
    single<CoreRepository> { CoreRepositoryImpl(get(), get(), get()) }
    single { GetAppStateUseCase(get()) }
    single { LoginUseCase(get()) }

    single { HomeScreenViewModel(get(), get()) }
}