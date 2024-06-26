package pl.kapucyni.wolczyn.app.archive.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.kapucyni.wolczyn.app.archive.data.sources.FirestoreArchiveSource
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.domain.repository.ArchiveRepository

class ArchiveRepositoryImpl(
    private val firestoreSource: FirestoreArchiveSource,
) : ArchiveRepository {

    override fun getArchive(): Flow<List<ArchiveMeeting>> =
        firestoreSource.getArchive()
            .map { meetings -> meetings.sortedByDescending { it.number } }

    override fun getMeetingByNumber(number: Int): Flow<ArchiveMeeting?> =
        firestoreSource.getArchiveMeetingByNumber(number)
}