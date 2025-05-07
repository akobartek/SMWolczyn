package pl.kapucyni.wolczyn.app.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppConfiguration(
    val appVersion: AppVersion = AppVersion.NO_MEETING,
    val androidForceUpdate: String? = null,
    val appleForceUpdate: String? = null,
    val openSigning: Int? = null,
)

expect fun AppConfiguration.platformForceUpdate(): String?