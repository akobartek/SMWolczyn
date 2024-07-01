package pl.kapucyni.wolczyn.app.songbook.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.kapucyni.wolczyn.app.songbook.data.repository.WolczynSongBookRepository
import pl.kapucyni.wolczyn.app.songbook.domain.repository.SongBookRepository
import pl.kapucyni.wolczyn.app.songbook.domain.usecases.FilterSongsUseCase
import pl.kapucyni.wolczyn.app.songbook.presentation.SongBookViewModel

val songBookModule = module {
    single<SongBookRepository> { WolczynSongBookRepository() }
    single { FilterSongsUseCase(get()) }

    viewModel { SongBookViewModel(get()) }
}