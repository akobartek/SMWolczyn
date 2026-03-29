package pl.kapucyni.wolczyn.app.meetings.presentation.signings.user

sealed interface SigningsEvent {
    data object NavigateUp : SigningsEvent
    data object UserNotAvailable : SigningsEvent
}