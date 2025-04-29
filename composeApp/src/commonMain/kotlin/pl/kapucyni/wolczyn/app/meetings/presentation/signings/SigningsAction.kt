package pl.kapucyni.wolczyn.app.meetings.presentation.signings

sealed interface SigningsAction {
    data class UpdateSomething(val xd: String) : SigningsAction
    data object SaveData : SigningsAction
    data object ToggleNoInternetDialog : SigningsAction
}