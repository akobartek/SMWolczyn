package pl.kapucyni.wolczyn.app.songbook.domain.usecases

import pl.kapucyni.wolczyn.app.songbook.domain.model.Song
import pl.kapucyni.wolczyn.app.songbook.domain.repository.SongBookRepository

class FilterSongsUseCase(private val songBookRepository: SongBookRepository) {
    operator fun invoke(query: String): List<Song> = songBookRepository.filterSongs(query)
}