package pl.kapucyni.wolczyn.app.songbook.domain.repository

import pl.kapucyni.wolczyn.app.songbook.domain.model.Song

interface SongBookRepository {
    fun filterSongs(query: String): List<Song>
}