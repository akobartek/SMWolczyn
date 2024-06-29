package pl.kapucyni.wolczyn.app.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WolczynGroup(
    val group: Int?,
    val persons: List<WolczynGroupMember>?,
)