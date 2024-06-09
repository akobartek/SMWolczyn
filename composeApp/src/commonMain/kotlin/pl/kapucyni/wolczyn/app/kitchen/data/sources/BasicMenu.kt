package pl.kapucyni.wolczyn.app.kitchen.data.sources

import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenuItem
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenuSection

internal val basicMenu = KitchenMenu(
    menu = mapOf(
        KitchenMenuSection.SNACKS to listOf(
            KitchenMenuItem(name = "Pierogi", variants = "ruskie / z mięsem / z owocami"),
            KitchenMenuItem(name = "Frytki z ketchupem"),
            KitchenMenuItem(name = "Frytki z sosem"),
            KitchenMenuItem(name = "Gotowana kukurydza"),
            KitchenMenuItem(name = "Tosty"),
            KitchenMenuItem(name = "Hot dog"),
            KitchenMenuItem(name = "Popcorn"),
        ),
        KitchenMenuSection.SWEETS to listOf(
            KitchenMenuItem(name = "Gofry", variants = "z bitą śmietaną / cukrem pudrem / owocami"),
            KitchenMenuItem(name = "Naleśniki"),
            KitchenMenuItem(name = "Wata cukrowa"),
        ),
        KitchenMenuSection.BEVERAGES to listOf(
            KitchenMenuItem(name = "Kawa"),
            KitchenMenuItem(name = "Kawa mrożona"),
            KitchenMenuItem(name = "Coca-cola"),
            KitchenMenuItem(name = "Tymbark"),
            KitchenMenuItem(name = "Nestea"),
        ),
    )
)