package pl.kapucyni.wolczyn.app.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeNotification(
    val message: String = "",
    val url: String = "",
)
