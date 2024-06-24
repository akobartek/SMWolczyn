package pl.kapucyni.wolczyn.app.breviary.di

import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.breviary.data.repository.BreviaryRepositoryImpl
import pl.kapucyni.wolczyn.app.breviary.data.sources.WebBreviarySource
import pl.kapucyni.wolczyn.app.breviary.domain.repository.BreviaryRepository
import pl.kapucyni.wolczyn.app.breviary.domain.usecases.CheckOfficesUseCase
import pl.kapucyni.wolczyn.app.breviary.domain.usecases.LoadBreviaryUseCase
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviarySelectViewModel
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviaryTextViewModel

val breviaryModule = module {
    single { WebBreviarySource() }
    single<BreviaryRepository> { BreviaryRepositoryImpl(get()) }
    single { CheckOfficesUseCase(get()) }
    single { LoadBreviaryUseCase(get()) }

    factory { BreviarySelectViewModel() }
    factory { BreviaryTextViewModel(get(), get()) }
}