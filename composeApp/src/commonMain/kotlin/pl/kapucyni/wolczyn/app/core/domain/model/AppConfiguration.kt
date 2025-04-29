package pl.kapucyni.wolczyn.app.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppConfiguration(
    val appVersion: AppVersion = AppVersion.NO_MEETING,
    val openSigning: Int? = null,
)
