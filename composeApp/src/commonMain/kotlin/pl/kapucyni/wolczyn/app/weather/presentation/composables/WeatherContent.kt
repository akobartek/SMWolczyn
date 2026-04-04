package pl.kapucyni.wolczyn.app.weather.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.vectorResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_weather_cloudy
import smwolczyn.composeapp.generated.resources.ic_weather_fog
import smwolczyn.composeapp.generated.resources.ic_weather_mostly_sunny
import smwolczyn.composeapp.generated.resources.ic_weather_rain
import smwolczyn.composeapp.generated.resources.ic_weather_rain_wind
import smwolczyn.composeapp.generated.resources.ic_weather_sandstorm
import smwolczyn.composeapp.generated.resources.ic_weather_showers
import smwolczyn.composeapp.generated.resources.ic_weather_snow
import smwolczyn.composeapp.generated.resources.ic_weather_sprinkle
import smwolczyn.composeapp.generated.resources.ic_weather_storm_showers
import smwolczyn.composeapp.generated.resources.ic_weather_sunny
import smwolczyn.composeapp.generated.resources.ic_weather_thunderstorm
import smwolczyn.composeapp.generated.resources.ic_weather_tsunami

@Composable
fun WeatherContent(weather: Weather) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 2.dp)
    ) {
        WolczynText(
            text = "${weather.dayTemp}°/${weather.nightTemp}°",
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
        Icon(
            vectorResource(
                when (weather.weatherCode) {
                    0 -> Res.drawable.ic_weather_sunny
                    in 1..19 -> Res.drawable.ic_weather_mostly_sunny
                    in 20..29 -> Res.drawable.ic_weather_cloudy
                    in 30..39 -> Res.drawable.ic_weather_sandstorm
                    in 40..49 -> Res.drawable.ic_weather_fog
                    in 50..59 -> Res.drawable.ic_weather_sprinkle
                    65 -> Res.drawable.ic_weather_rain_wind
                    in 60..69 -> Res.drawable.ic_weather_rain
                    in 70..79 -> Res.drawable.ic_weather_snow
                    in 80..94 -> Res.drawable.ic_weather_showers
                    95, 97, 98 -> Res.drawable.ic_weather_thunderstorm
                    96, 99 -> Res.drawable.ic_weather_storm_showers
                    else -> Res.drawable.ic_weather_tsunami
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .offset(y = (-4).dp),
        )
        if (weather.precipitationChance > 10)
            WolczynText(
                text = "${weather.precipitationChance}%",
                textStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier.offset(y = (-4).dp)
            )
    }
}