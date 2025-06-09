package pl.kapucyni.wolczyn.app.meetings.presentation.participants.list

import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant

sealed interface ParticipantsScreenEvent {
    data class ScanUserFound(val participant: Participant) : ParticipantsScreenEvent
}