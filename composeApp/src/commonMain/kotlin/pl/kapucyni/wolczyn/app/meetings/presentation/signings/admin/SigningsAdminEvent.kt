package pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin

sealed interface SigningsAdminEvent {
    data object NavigateUp : SigningsAdminEvent
}