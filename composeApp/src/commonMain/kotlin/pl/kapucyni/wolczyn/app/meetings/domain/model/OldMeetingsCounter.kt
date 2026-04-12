package pl.kapucyni.wolczyn.app.meetings.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OldMeetingsCounter(
    val pesel: String,
    val counter: Int,
)
