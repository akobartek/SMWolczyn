package pl.kapucyni.wolczyn.app.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WolczynUser(
    val type: Int?,
    val state: Int?,
    val status: Int?,
    val group: Int?,
    val name: String?,
    val surname: String?,
    val prefix: String?,
    @SerialName("photo_url") val photoUrl: String?,
    val number: Int?,
    val bears: Int?,
)