package pl.kapucyni.wolczyn.app.common.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FirestorePromotion(
    val id: String = "",
    val name: String = "",
    val isValid: Boolean = false,
)
