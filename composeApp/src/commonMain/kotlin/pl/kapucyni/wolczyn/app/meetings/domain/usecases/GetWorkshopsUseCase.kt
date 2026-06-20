package pl.kapucyni.wolczyn.app.meetings.domain.usecases

import kotlinx.coroutines.flow.combine
import pl.kapucyni.wolczyn.app.meetings.domain.MeetingsRepository

class GetWorkshopsUseCase(private val meetingsRepository: MeetingsRepository) {

    operator fun invoke(meetingId: Int, takeOnlyAvailable: Boolean = false) =
        combine(
            meetingsRepository.getMeetingParticipants(meetingId),
            meetingsRepository.getWorkshopsFlow(meetingId)
        ) { participants, workshops ->
            workshops
                .filter { workshop ->
                    if (takeOnlyAvailable) workshop.available
                    else true
                }
                .map {
                    it to participants.count { participant -> participant.workshop == it.name }
                }

        }
}