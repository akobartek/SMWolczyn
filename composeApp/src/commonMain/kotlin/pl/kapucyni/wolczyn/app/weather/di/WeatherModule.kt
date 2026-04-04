package pl.kapucyni.wolczyn.app.weather.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.weather.data.repository.WeatherRepositoryImpl
import pl.kapucyni.wolczyn.app.weather.data.sources.FirestoreWeatherSource
import pl.kapucyni.wolczyn.app.weather.data.sources.OpenMeteoWeatherSource
import pl.kapucyni.wolczyn.app.weather.domain.repository.WeatherRepository
import pl.kapucyni.wolczyn.app.weather.domain.usecases.GetWeatherUseCase
import pl.kapucyni.wolczyn.app.weather.presentation.WeatherViewModel

val weatherModule = module {
    singleOf(::FirestoreWeatherSource)
    singleOf(::OpenMeteoWeatherSource)
    singleOf(::WeatherRepositoryImpl) bind WeatherRepository::class

    factoryOf(::GetWeatherUseCase)

    viewModelOf(::WeatherViewModel)
}