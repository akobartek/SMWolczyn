package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

sealed interface ParticipantDetailsScreenEvent {
    data object NavigateUp : ParticipantDetailsScreenEvent
}