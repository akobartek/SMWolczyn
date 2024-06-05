package pl.kapucyni.wolczyn.app.songbook.domain.usecase

import pl.kapucyni.wolczyn.app.songbook.domain.repository.SongBookRepository

class FilterSongsUseCase(private val songBookRepository: SongBookRepository) {
    operator fun invoke(query: String) = songBookRepository.filterSongs(query)
}