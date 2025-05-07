package pl.kapucyni.wolczyn.app.meetings.domain.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Meeting(
    val id: Int = 0,
    val name: String = "",
    val start: Timestamp = Timestamp.now(),
    val end: Timestamp = Timestamp.now(),
    val photoUrl: String = "",
)
