package pl.kapucyni.wolczyn.app.common.presentation

import pl.kapucyni.wolczyn.app.common.presentation.Screen.*

enum class HomeTileType(val navRoute: Screen) {
    SCHEDULE(Schedule),
    SONG_BOOK(SongBook),
    KITCHEN(Kitchen),
    SHOP(Shop),
    DECALOGUE(Decalogue),
    WEATHER(Admin),
    BREVIARY(BreviarySelect),
    ARCHIVE(Archive),
}