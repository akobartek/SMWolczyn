package pl.kapucyni.wolczyn.app.meetings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Workshop(
    val id: String = "",
    val available: Boolean = false,
    val name: String = "",
)
