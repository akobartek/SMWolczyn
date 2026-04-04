package pl.kapucyni.wolczyn.app.common.presentation.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pl.kapucyni.wolczyn.app.meetings.domain.model.Participant
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType

internal val ParticipantParameterType = object: NavType<Participant?>(isNullableAllowed = true) {

    override fun put(bundle: SavedState, key: String, value: Participant?) {
        bundle.write { putString(key, serializeAsValue(value)) }
    }

    override fun get(bundle: SavedState, key: String): Participant? =
        bundle.read { getStringOrNull(key) }?.let { parseValue(it) }

    override fun parseValue(value: String): Participant? =
        (Json.decodeFromString(value) as NavParticipant?)?.toParticipant()

    override fun serializeAsValue(value: Participant?) =
        Json.encodeToString(value?.let { NavParticipant.fromParticipant(it) })
}

@Serializable
private data class NavParticipant(
    val userId: String = "",
    val type: ParticipantType = ParticipantType.MEMBER,
    val firstName: String = "",
    val lastName: String = "",
    val city: String = "",
    val email: String = "",
    val pesel: String = "",
    val contactNumber: String = "",
    val workshop: String = "",
    val birthday: Double,
    val notes: String = "",
    val createdAt: Double,
    val paid: Boolean = false,
    val consents: Boolean = false,
    val underageConsents: Boolean = false,
    val acceptedAt: Double? = null,
    val acceptedBy: String? = null,
    val acceptedById: String? = null,
) {
    fun toParticipant() = Participant(
        userId, type, firstName, lastName,
        city, email, pesel, contactNumber, workshop,
        Timestamp.fromMilliseconds(birthday), notes,
        Timestamp.fromMilliseconds(createdAt), paid, consents,
        underageConsents, acceptedAt?.let { Timestamp.fromMilliseconds(it) },
        acceptedBy, acceptedById,
    )

    companion object {
        fun fromParticipant(participant: Participant) = with(participant) {
            NavParticipant(
                userId, type, firstName, lastName,
                city, email, pesel, contactNumber, workshop,
                birthday.toMilliseconds(), notes,
                createdAt.toMilliseconds(), paid, consents,
                underageConsents, acceptedAt?.toMilliseconds(),
                acceptedBy, acceptedById,
            )
        }
    }
}