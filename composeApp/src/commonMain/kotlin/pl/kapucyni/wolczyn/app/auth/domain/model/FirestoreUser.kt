package pl.kapucyni.wolczyn.app.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FirestoreUser(
    val email: String? = null,
    val userType: UserType = UserType.MEMBER
) {
    companion object {
        fun createUser(email: String) = FirestoreUser(email, UserType.MEMBER)
    }
}