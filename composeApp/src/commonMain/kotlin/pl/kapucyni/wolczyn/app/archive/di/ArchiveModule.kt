package pl.kapucyni.wolczyn.app.archive.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.archive.data.repository.ArchiveRepositoryImpl
import pl.kapucyni.wolczyn.app.archive.data.sources.FirestoreArchiveSource
import pl.kapucyni.wolczyn.app.archive.domain.repository.ArchiveRepository
import pl.kapucyni.wolczyn.app.archive.domain.usecases.GetArchiveMeetingByNumberUseCase
import pl.kapucyni.wolczyn.app.archive.domain.usecases.GetArchiveUseCase
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveMeetingViewModel
import pl.kapucyni.wolczyn.app.archive.presentation.ArchiveViewModel

val archiveModule = module {
    single { FirestoreArchiveSource() }
    single<ArchiveRepository> { ArchiveRepositoryImpl(get()) }
    single { GetArchiveUseCase(get()) }
    single { GetArchiveMeetingByNumberUseCase(get()) }

    viewModel { ArchiveViewModel(get()) }
    viewModel { ArchiveMeetingViewModel(get()) }
}