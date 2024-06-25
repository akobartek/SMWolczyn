package pl.kapucyni.wolczyn.app.kitchen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FirestoreMenuPromotion(
    val id: String = "",
    val name: String = "",
    val isValid: Boolean = true,
)
