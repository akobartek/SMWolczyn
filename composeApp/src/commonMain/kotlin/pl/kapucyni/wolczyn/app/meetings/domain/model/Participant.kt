package pl.kapucyni.wolczyn.app.meetings.domain.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class Participant(
    val userId: String = "",
    val type: ParticipantType = ParticipantType.MEMBER,
    val firstName: String = "",
    val lastName: String = "",
    val city: String = "",
    val email: String = "",
    val pesel: String = "",
    val contactNumber: String = "",
    val workshop: String = "",
    val birthday: Timestamp = Timestamp.now(),
    val createdAt: Timestamp = Timestamp.now(),
    val consents: Boolean = false,
    val underageConsents: Boolean = false,
)