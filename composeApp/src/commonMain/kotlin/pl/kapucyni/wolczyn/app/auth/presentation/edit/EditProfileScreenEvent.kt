package pl.kapucyni.wolczyn.app.auth.presentation.edit

sealed interface EditProfileScreenEvent {
    data object NavigateUp : EditProfileScreenEvent
}
