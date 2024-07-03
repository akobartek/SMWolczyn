package pl.kapucyni.wolczyn.app.breviary.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.breviary.data.database.BreviaryDao
import pl.kapucyni.wolczyn.app.breviary.data.database.BreviaryDatabase
import pl.kapucyni.wolczyn.app.breviary.data.repository.BreviaryRepositoryImpl
import pl.kapucyni.wolczyn.app.breviary.data.sources.DbBreviarySource
import pl.kapucyni.wolczyn.app.breviary.data.sources.WebBreviarySource
import pl.kapucyni.wolczyn.app.breviary.domain.repository.BreviaryRepository
import pl.kapucyni.wolczyn.app.breviary.domain.usecases.CheckOfficesUseCase
import pl.kapucyni.wolczyn.app.breviary.domain.usecases.ClearBreviaryDbUseCase
import pl.kapucyni.wolczyn.app.breviary.domain.usecases.LoadBreviaryUseCase
import pl.kapucyni.wolczyn.app.breviary.domain.usecases.SaveBreviaryUseCase
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviarySelectViewModel
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviaryTextViewModel

val breviaryModule = module {
    single<BreviaryDao> {
        get<BreviaryDatabase>().breviaryDao()
    }

    single { WebBreviarySource() }
    single { DbBreviarySource(get()) }
    single<BreviaryRepository> { BreviaryRepositoryImpl(get(), get()) }
    single { CheckOfficesUseCase(get()) }
    single { LoadBreviaryUseCase(get()) }
    single { SaveBreviaryUseCase(get()) }
    single { ClearBreviaryDbUseCase(get()) }

    viewModel { BreviarySelectViewModel(get()) }
    viewModel { BreviaryTextViewModel(get(), get()) }
}