package pl.kapucyni.wolczyn.app.common.presentation

import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.ADMIN_ROUTE
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.ARCHIVE_ROUTE
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.BREVIARY_SELECT_ROUTE
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.DECALOGUE_ROUTE
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.KITCHEN_ROUTE
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.SCHEDULE_ROUTE
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.SHOP_ROUTE
import pl.kapucyni.wolczyn.app.common.presentation.Screen.Companion.SONG_BOOK_ROUTE

enum class HomeTileType(val navRoute: String) {
    SCHEDULE(SCHEDULE_ROUTE),
    SONG_BOOK(SONG_BOOK_ROUTE),
    KITCHEN(KITCHEN_ROUTE),
    SHOP(SHOP_ROUTE),
    DECALOGUE(DECALOGUE_ROUTE),
    WEATHER(ADMIN_ROUTE),
    BREVIARY(BREVIARY_SELECT_ROUTE),
    ARCHIVE(ARCHIVE_ROUTE)
}