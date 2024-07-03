package pl.kapucyni.wolczyn.app.core.domain.model

data class AppState(
    val configuration: AppConfiguration,
    val notifications: List<HomeNotification>,
) {
    companion object {
        fun init(
            configuration: AppConfiguration?,
            notifications: List<HomeNotification>,
        ) = AppState(
            configuration = configuration ?: AppConfiguration(),
            notifications = notifications,
        )
    }
}