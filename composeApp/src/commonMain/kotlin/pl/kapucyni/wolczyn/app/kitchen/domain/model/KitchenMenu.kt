package pl.kapucyni.wolczyn.app.kitchen.domain.model

data class KitchenMenu(
    val menu: Map<KitchenMenuSection, List<KitchenMenuItem>> = mapOf(),
    val promotions: List<String> = listOf()
)
