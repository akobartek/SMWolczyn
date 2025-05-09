package pl.kapucyni.wolczyn.app.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import pl.kapucyni.wolczyn.app.archive.presentation.composables.ArchiveHomeTile
import pl.kapucyni.wolczyn.app.breviary.presentation.composables.BreviaryHomeTile
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType.*
import pl.kapucyni.wolczyn.app.decalogue.presentation.composables.DecalogueHomeTile
import pl.kapucyni.wolczyn.app.kitchen.presentation.composables.KitchenHomeTile
import pl.kapucyni.wolczyn.app.meetings.presentation.meetings.composables.MeetingsHomeTile
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables.SigningsHomeTile
import pl.kapucyni.wolczyn.app.schedule.presentation.composables.ScheduleHomeTile
import pl.kapucyni.wolczyn.app.shop.presentation.composables.ShopHomeTile
import pl.kapucyni.wolczyn.app.songbook.presentation.composables.SongBookHomeTile
import pl.kapucyni.wolczyn.app.weather.presentation.composables.WeatherHomeTile

@Composable
fun getHomeTile(
    tileType: HomeTileType,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (tileType) {
        SIGNING -> SigningsHomeTile(backgroundColor, onClick, modifier)
        MEETINGS -> MeetingsHomeTile(backgroundColor, onClick, modifier)
        SCHEDULE -> ScheduleHomeTile(backgroundColor, onClick, modifier)
        SONG_BOOK -> SongBookHomeTile(backgroundColor, onClick, modifier)
        KITCHEN -> KitchenHomeTile(backgroundColor, onClick, modifier)
        SHOP -> ShopHomeTile(backgroundColor, onClick, modifier)
        DECALOGUE -> DecalogueHomeTile(backgroundColor, onClick, modifier)
        WEATHER -> WeatherHomeTile(backgroundColor, onClick, modifier)
        BREVIARY -> BreviaryHomeTile(backgroundColor, onClick, modifier)
        ARCHIVE -> ArchiveHomeTile(backgroundColor, onClick, modifier)
    }
}