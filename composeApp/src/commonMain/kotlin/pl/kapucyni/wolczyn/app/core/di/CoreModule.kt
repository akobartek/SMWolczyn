package pl.kapucyni.wolczyn.app.core.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.core.data.repository.CoreRepositoryImpl
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.data.sources.WolczynApi
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetAppStateUseCase
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetGroupInfoUseCase
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetUserInfoUseCase
import pl.kapucyni.wolczyn.app.core.domain.usecases.SignInUseCase
import pl.kapucyni.wolczyn.app.core.domain.usecases.SignOutUseCase
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
    single { GetUserInfoUseCase(get()) }
    single { GetGroupInfoUseCase(get()) }
    single { SignInUseCase(get()) }
    single { SignOutUseCase(get()) }

    viewModel { HomeScreenViewModel(get(), get(), get(), get(), get()) }
}