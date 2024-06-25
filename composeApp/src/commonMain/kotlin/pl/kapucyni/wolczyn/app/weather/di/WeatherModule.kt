package pl.kapucyni.wolczyn.app.weather.di

import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.weather.data.repository.WeatherRepositoryImpl
import pl.kapucyni.wolczyn.app.weather.data.sources.FirestoreWeatherSource
import pl.kapucyni.wolczyn.app.weather.domain.repository.WeatherRepository
import pl.kapucyni.wolczyn.app.weather.domain.usecases.GetWeatherUseCase
import pl.kapucyni.wolczyn.app.weather.presentation.WeatherViewModel

val weatherModule = module {
    single { FirestoreWeatherSource() }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single { GetWeatherUseCase(get()) }

    single { WeatherViewModel(get()) }
}