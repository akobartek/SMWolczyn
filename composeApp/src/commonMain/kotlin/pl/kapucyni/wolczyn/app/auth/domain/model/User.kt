package pl.kapucyni.wolczyn.app.auth.domain.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class User(
    val id: String = "",
    val publicId: Int = 0,
    val email: String = "",
    @SerialName("userType")
    val userTypeString: String = UserType.MEMBER.name,
    val firstName: String = "",
    val lastName: String = "",
    val city: String = "",
    val permits: List<UserPermit> = listOf(),
    val birthday: Timestamp? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val photoUrl: String? = null,
) {

    @Transient
    val userType: UserType
        get() = runCatching {
            UserType.valueOf(userTypeString)
        }.getOrDefault(UserType.MEMBER)

    fun isAdmin() = userType == UserType.ADMIN

    fun hasAccessToParticipantsData() = isAdmin()
            || permits.contains(UserPermit.SIGNINGS)
            || permits.contains(UserPermit.VOLUNTEERS)
            || permits.contains(UserPermit.ANIMATORS)

    fun hasAccessToAllParticipants() = isAdmin()
            || permits.contains(UserPermit.SIGNINGS)
            || permits.contains(UserPermit.VOLUNTEERS)

    fun canEditParticipantsData() = isAdmin() || permits.contains(UserPermit.VOLUNTEERS)

    fun canEditParticipantsWorkshop() = isAdmin() || permits.contains(UserPermit.SIGNINGS)

    fun hasAnimatorsPermit() = permits.contains(UserPermit.ANIMATORS)

    fun hasWorkshopsPermit() = permits.contains(UserPermit.WORKSHOPS)

}