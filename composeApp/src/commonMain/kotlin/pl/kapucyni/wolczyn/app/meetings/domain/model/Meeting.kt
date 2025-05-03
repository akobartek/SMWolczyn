package pl.kapucyni.wolczyn.app.meetings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Meeting(
    val id: Int = 0,
    val name: String = "",
    val date: String = "",
    val photoUrl: String = "",
)
