package pl.kapucyni.wolczyn.app.core.domain.model

data class AppState(
    val configuration: AppConfiguration,
    val notifications: List<HomeNotification>,
)