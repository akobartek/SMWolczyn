package pl.kapucyni.wolczyn.app.archive.presentation

sealed interface ArchiveMeetingScreenEvent {
    data object NavigateUp : ArchiveMeetingScreenEvent
}