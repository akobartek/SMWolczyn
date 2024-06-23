package pl.kapucyni.wolczyn.app.schedule.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType
import pl.kapucyni.wolczyn.app.common.presentation.composables.WidthSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.schedule.domain.model.Event
import pl.kapucyni.wolczyn.app.schedule.domain.model.EventPlace
import pl.kapucyni.wolczyn.app.schedule.domain.model.EventType
import pl.kapucyni.wolczyn.app.theme.appColorSecondary
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_schedule_breviary
import smwolczyn.composeapp.generated.resources.ic_schedule_kitchen
import smwolczyn.composeapp.generated.resources.ic_schedule_map_pin
import smwolczyn.composeapp.generated.resources.ic_schedule_song_book
import smwolczyn.composeapp.generated.resources.place_amphitheatre
import smwolczyn.composeapp.generated.resources.place_campsite
import smwolczyn.composeapp.generated.resources.place_church
import smwolczyn.composeapp.generated.resources.place_court
import smwolczyn.composeapp.generated.resources.place_everywhere
import smwolczyn.composeapp.generated.resources.place_unknown
import smwolczyn.composeapp.generated.resources.place_white_tent

@Composable
fun EventCard(
    event: Event,
    filledCircle: Boolean,
    hideTime: Boolean,
    isLast: Boolean,
    onNavClick: (HomeTileType) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    var cardHeight by remember { mutableStateOf(0) }
    val cardHeightDp = with(LocalDensity.current) { cardHeight.toDp() }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!hideTime)
            WolczynText(
                text = "${event.time.hour}.${event.time.minute}".replace(".0", ".00"),
                textStyle = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(top = 12.dp)
            )

        EventTimeMarker(
            filledCircle = filledCircle,
            isLast = isLast,
            modifier = Modifier
                .padding(start = 72.dp)
                .size(width = 16.dp, height = cardHeightDp)
                .offset(x = 0.dp, y = 26.dp)
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = appColorSecondary
            ),
            onClick = {
                when (event.type) {
                    EventType.MASS -> onNavClick(HomeTileType.SONG_BOOK)
                    EventType.MEAL -> onNavClick(HomeTileType.KITCHEN)
                    EventType.BREVIARY -> onNavClick(HomeTileType.BREVIARY)
                    else -> {
                        if (!event.guestUrl.isNullOrBlank())
                            uriHandler.openUri(event.guestUrl)
                    }
                }
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(start = 92.dp, bottom = 12.dp)
                .onGloballyPositioned { cardHeight = it.size.height }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    WolczynText(
                        text = event.name,
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    event.guest?.let { guest ->
                        WolczynText(
                            text = guest,
                            textStyle = TextStyle(
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                    WidthSpacer(4.dp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_schedule_map_pin),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        WidthSpacer(2.dp)
                        when (event.place) {
                            EventPlace.AMPHITHEATRE -> Res.string.place_amphitheatre
                            EventPlace.WHITE_TENT -> Res.string.place_white_tent
                            EventPlace.CAMPSITE -> Res.string.place_campsite
                            EventPlace.CHURCH -> Res.string.place_church
                            EventPlace.EVERYWHERE -> Res.string.place_everywhere
                            EventPlace.COURT -> Res.string.place_court
                            EventPlace.UNKNOWN -> Res.string.place_unknown
                        }.let {
                            WolczynText(
                                text = stringResource(it),
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            )
                        }
                    }
                }
                when (event.type) {
                    EventType.CONFERENCE,
                    EventType.CONCERT,
                    EventType.DEVOTION,
                    EventType.ORGANIZATION,
                    EventType.GROUPS,
                    EventType.WORKSHOPS,
                    EventType.PRAYER,
                    EventType.MF_TAU,
                    EventType.GUEST_TALK,
                    EventType.OTHER -> null

                    EventType.MASS -> Res.drawable.ic_schedule_song_book
                    EventType.MEAL -> Res.drawable.ic_schedule_kitchen
                    EventType.BREVIARY -> Res.drawable.ic_schedule_breviary
                }?.let { drawable ->
                    Icon(
                        painter = painterResource(drawable),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        }
    }
}