package pl.kapucyni.wolczyn.app.core.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.core.data.repository.CoreRepositoryImpl
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.data.sources.WolczynApi
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository
import pl.kapucyni.wolczyn.app.core.domain.usecases.GetAppStateUseCase
import pl.kapucyni.wolczyn.app.core.presentation.HomeScreenViewModel

val coreModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                url("https://api.kapucyni.pl/")
            }
        }
    }

    single { FirestoreHomeSource() }
    single { WolczynApi(get()) }
    single<CoreRepository> { CoreRepositoryImpl(get(), get()) }
    single { GetAppStateUseCase(get()) }

    single { HomeScreenViewModel(get()) }
}