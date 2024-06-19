package pl.kapucyni.wolczyn.app.kitchen.data.sources

import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenuItem
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenuSection

internal fun getBasicMenu() = KitchenMenu(
    menu = mapOf(
        KitchenMenuSection.SNACKS to listOf(
            KitchenMenuItem(
                id = "snacks-1",
                name = "Pierogi",
                variants = "ruskie / z mięsem / z owocami"
            ),
            KitchenMenuItem(id = "snacks-2", name = "Frytki z ketchupem"),
            KitchenMenuItem(id = "snacks-3", name = "Frytki z sosem"),
            KitchenMenuItem(id = "snacks-4", name = "Gotowana kukurydza"),
            KitchenMenuItem(id = "snacks-5", name = "Tosty"),
            KitchenMenuItem(id = "snacks-6", name = "Hot dog"),
            KitchenMenuItem(id = "snacks-7", name = "Popcorn"),
        ),
        KitchenMenuSection.SWEETS to listOf(
            KitchenMenuItem(
                id = "sweets-1",
                name = "Gofry",
                variants = "z bitą śmietaną / cukrem pudrem / owocami"
            ),
            KitchenMenuItem(id = "sweets-2", name = "Naleśniki"),
            KitchenMenuItem(id = "sweets-3", name = "Wata cukrowa"),
        ),
        KitchenMenuSection.BEVERAGES to listOf(
            KitchenMenuItem(id = "beverages-1", name = "Kawa"),
            KitchenMenuItem(id = "beverages-2", name = "Kawa mrożona"),
            KitchenMenuItem(id = "beverages-3", name = "Coca-cola"),
            KitchenMenuItem(id = "beverages-4", name = "Tymbark"),
            KitchenMenuItem(id = "beverages-5", name = "Nestea"),
        ),
    )
)