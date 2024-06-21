package pl.kapucyni.wolczyn.app.archive.domain.usecases

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.domain.repository.ArchiveRepository

class GetArchiveUseCase(private val archiveRepository: ArchiveRepository) {
    operator fun invoke(): Flow<List<ArchiveMeeting>> = archiveRepository.getArchive()
}