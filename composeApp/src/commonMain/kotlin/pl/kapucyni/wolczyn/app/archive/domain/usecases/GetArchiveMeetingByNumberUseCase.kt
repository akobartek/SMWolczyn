package pl.kapucyni.wolczyn.app.archive.domain.usecases

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.archive.domain.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.archive.domain.repository.ArchiveRepository

class GetArchiveMeetingByNumberUseCase(private val archiveRepository: ArchiveRepository) {
    operator fun invoke(number: Int): Flow<ArchiveMeeting?> =
        archiveRepository.getMeetingByNumber(number)
}