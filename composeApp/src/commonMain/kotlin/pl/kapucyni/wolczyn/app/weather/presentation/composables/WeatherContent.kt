package pl.kapucyni.wolczyn.app.weather.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather

@Composable
fun WeatherContent(weather: Weather) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        WolczynText(
            text = "${weather.dayTemp}°/${weather.nightTemp}°",
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
        AsyncImage(
            model = weather.iconUrl,
            contentDescription = null,
            modifier = Modifier.size(48.dp).offset(y = (-8).dp)
        )
        if (weather.precipitationChance > 10)
            WolczynText(
                text = "${weather.precipitationChance}%",
                textStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier.offset(y = (-16).dp)
            )
    }
}