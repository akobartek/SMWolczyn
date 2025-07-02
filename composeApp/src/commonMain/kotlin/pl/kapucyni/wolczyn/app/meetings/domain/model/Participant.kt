package pl.kapucyni.wolczyn.app.meetings.domain.model

import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.serialization.Serializable
import pl.kapucyni.wolczyn.app.common.utils.getAge
import pl.kapucyni.wolczyn.app.common.utils.isAgeBelow

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
    val paid: Boolean = false,
    val consents: Boolean = false,
    val underageConsents: Boolean = false,
) {
    fun isUnderAge() = birthday.toMilliseconds().toLong().isAgeBelow(18)

    fun getAge() = birthday.toMilliseconds().toLong().getAge()
}