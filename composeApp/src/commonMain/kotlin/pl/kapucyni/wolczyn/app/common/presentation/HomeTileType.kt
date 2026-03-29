package pl.kapucyni.wolczyn.app.common.presentation

import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration

enum class HomeTileType {
    SIGNING,
    MEETINGS,
    SCHEDULE,
    SONG_BOOK,
    KITCHEN,
    SHOP,
    DECALOGUE,
    WEATHER,
    BREVIARY,
    ARCHIVE,
    ;
    
    fun navRoute(appConfiguration: AppConfiguration) = when(this) {
        SIGNING -> appConfiguration.openSigning?.let { Screen.Signings(meetingId = it) }
        MEETINGS -> Screen.Meetings
        SCHEDULE -> Screen.Schedule
        SONG_BOOK -> Screen.SongBook
        KITCHEN -> Screen.Kitchen
        SHOP -> Screen.Shop
        DECALOGUE -> Screen.Decalogue
        WEATHER -> Screen.Admin
        BREVIARY -> Screen.BreviarySelect
        ARCHIVE -> Screen.Archive
    }
}