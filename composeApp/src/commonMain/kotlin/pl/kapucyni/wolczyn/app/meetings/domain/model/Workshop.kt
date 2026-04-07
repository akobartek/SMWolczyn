package pl.kapucyni.wolczyn.app.meetings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Workshop(
    val id: String = "",
    val available: Boolean = false,
    val name: String = "",
    val gender: Gender = Gender.BOTH,
) {
    fun allow(gender: Gender) = this.gender == Gender.BOTH || this.gender == gender
}
