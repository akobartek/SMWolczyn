package pl.kapucyni.wolczyn.app.kitchen.data.model

import kotlinx.serialization.Serializable
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenuItem
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenuSection

@Serializable
data class FirestoreMenuItem(
    val id: String = "",
    val name: String = "",
    val variants: String = "",
    val isAvailable: Boolean = true,
    val section: KitchenMenuSection = KitchenMenuSection.SNACKS
) {
    fun toDomainObject() = KitchenMenuItem(
        id = id,
        name = name,
        variants = variants
    )
}
