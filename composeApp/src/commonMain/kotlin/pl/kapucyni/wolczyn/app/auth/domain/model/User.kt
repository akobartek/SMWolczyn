package pl.kapucyni.wolczyn.app.auth.domain.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val publicId: Int = 0,
    val email: String = "",
    val userType: UserType = UserType.MEMBER,
    val firstName: String = "",
    val lastName: String = "",
    val city: String = "",
    val birthday: Timestamp? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val photoUrl: String? = null,
)