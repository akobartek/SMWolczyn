package pl.kapucyni.wolczyn.app.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WolczynUser(
    @SerialName("user_id") val userId: String,
    val number: Int?,
    val name: String?,
    val surname: String?,
    val prefix: String?,
    val type: Int?,
    val state: Int?,
    val status: Int?,
    val group: Int?,
    @SerialName("photo_url") val photoUrl: String?,
)