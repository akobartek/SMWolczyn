package pl.kapucyni.wolczyn.app.core.presentation

import pl.kapucyni.wolczyn.app.theme.ARCHIVE_ROUTE
import pl.kapucyni.wolczyn.app.theme.BREVIARY_ROUTE
import pl.kapucyni.wolczyn.app.theme.DECALOGUE_ROUTE
import pl.kapucyni.wolczyn.app.theme.KITCHEN_ROUTE
import pl.kapucyni.wolczyn.app.theme.SCHEDULE_ROUTE
import pl.kapucyni.wolczyn.app.theme.SHOP_ROUTE
import pl.kapucyni.wolczyn.app.theme.SONG_BOOK_ROUTE
import pl.kapucyni.wolczyn.app.theme.WEATHER_ROUTE

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