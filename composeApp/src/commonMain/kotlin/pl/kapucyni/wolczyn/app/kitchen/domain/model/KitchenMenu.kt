package pl.kapucyni.wolczyn.app.kitchen.domain.model

import pl.kapucyni.wolczyn.app.quiz.domain.model.Quiz

data class KitchenMenu(
    val menu: Map<KitchenMenuSection, List<KitchenMenuItem>> = mapOf(),
    val promotions: List<String> = listOf(),
    val quiz: Quiz? = null,
)
