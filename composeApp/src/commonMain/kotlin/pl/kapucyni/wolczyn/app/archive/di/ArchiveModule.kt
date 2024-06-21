package pl.kapucyni.wolczyn.app.archive.di

import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.archive.data.repository.ArchiveRepositoryImpl
import pl.kapucyni.wolczyn.app.archive.domain.repository.ArchiveRepository
import pl.kapucyni.wolczyn.app.archive.domain.usecases.GetArchiveUseCase
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveViewModel

val archiveModule = module {
    single<ArchiveRepository> { ArchiveRepositoryImpl() }
    single { GetArchiveUseCase(get()) }

    single { ArchiveViewModel(get()) }
}