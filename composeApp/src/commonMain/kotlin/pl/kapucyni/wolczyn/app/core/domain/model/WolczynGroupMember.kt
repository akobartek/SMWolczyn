package pl.kapucyni.wolczyn.app.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WolczynGroupMember(
    val name: String?,
    val age: Int?,
    val city: String?,
)
