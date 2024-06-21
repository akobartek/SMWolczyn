package pl.kapucyni.wolczyn.app.archive.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kapucyni.wolczyn.app.archive.data.sources.getFirestoreArchive
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.domain.repository.ArchiveRepository

class ArchiveRepositoryImpl : ArchiveRepository {
    override fun getArchive(): Flow<List<ArchiveMeeting>> =
        getFirestoreArchive().map { meetings -> meetings.sortedByDescending { it.number } }
}