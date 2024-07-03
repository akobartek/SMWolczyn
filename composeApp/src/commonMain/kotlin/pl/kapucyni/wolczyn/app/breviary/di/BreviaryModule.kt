package pl.kapucyni.wolczyn.app.breviary.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviarySaveViewModel
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviarySelectViewModel
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviaryTextViewModel

val breviaryModule = module {
    single<BreviaryDatabase> {
        val builder: RoomDatabase.Builder<BreviaryDatabase> = get()
        builder
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
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
    viewModel { BreviarySaveViewModel(get(), get()) }
}