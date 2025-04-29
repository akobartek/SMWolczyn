package pl.kapucyni.wolczyn.app.meetings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Workshop(
    val available: Boolean = false,
    val name: String = "",
)
