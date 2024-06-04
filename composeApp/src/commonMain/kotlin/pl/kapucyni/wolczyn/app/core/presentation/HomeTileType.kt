package pl.kapucyni.wolczyn.app.core.presentation

import pl.kapucyni.wolczyn.app.theme.Screen.Companion.ARCHIVE_ROUTE
import pl.kapucyni.wolczyn.app.theme.Screen.Companion.BREVIARY_ROUTE
import pl.kapucyni.wolczyn.app.theme.Screen.Companion.DECALOGUE_ROUTE
import pl.kapucyni.wolczyn.app.theme.Screen.Companion.KITCHEN_ROUTE
import pl.kapucyni.wolczyn.app.theme.Screen.Companion.SCHEDULE_ROUTE
import pl.kapucyni.wolczyn.app.theme.Screen.Companion.SHOP_ROUTE
import pl.kapucyni.wolczyn.app.theme.Screen.Companion.SONG_BOOK_ROUTE
import pl.kapucyni.wolczyn.app.theme.Screen.Companion.WEATHER_ROUTE


enum class HomeTileType(val navRoute: String) {
    SCHEDULE(SCHEDULE_ROUTE),
    SONG_BOOK(SONG_BOOK_ROUTE),
    KITCHEN(KITCHEN_ROUTE),
    SHOP(SHOP_ROUTE),
    DECALOGUE(DECALOGUE_ROUTE),
    WEATHER(WEATHER_ROUTE),
    BREVIARY(BREVIARY_ROUTE),
    ARCHIVE(ARCHIVE_ROUTE)
}