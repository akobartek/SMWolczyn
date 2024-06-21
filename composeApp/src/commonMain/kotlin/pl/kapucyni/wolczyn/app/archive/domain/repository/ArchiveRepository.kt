package pl.kapucyni.wolczyn.app.archive.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting

interface ArchiveRepository {
    fun getArchive(): Flow<List<ArchiveMeeting>>
    fun getMeetingByNumber(number: Int): Flow<ArchiveMeeting?>
}