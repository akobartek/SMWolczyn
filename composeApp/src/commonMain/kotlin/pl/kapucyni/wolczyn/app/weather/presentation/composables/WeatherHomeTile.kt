package pl.kapucyni.wolczyn.app.weather.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.composables.HomeTile
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather
import pl.kapucyni.wolczyn.app.weather.presentation.WeatherViewModel
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_cap_weather
import smwolczyn.composeapp.generated.resources.weather_title

@Composable
fun WeatherHomeTile(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = koinInject()
) {
    val weather by weatherViewModel.screenState.collectAsStateMultiplatform()
    WeatherHomeTileContent(
        backgroundColor = backgroundColor,
        weather = weather,
        modifier = modifier
    )
}

@Composable
fun WeatherHomeTileContent(
    backgroundColor: Color,
    weather: Weather?,
    modifier: Modifier = Modifier
) {
    HomeTile(
        nameRes = Res.string.weather_title,
        nameAlignment = Alignment.TopEnd,
        height = 170.dp,
        backgroundColor = backgroundColor,
        image = {
            Image(
                painter = painterResource(Res.drawable.ic_cap_weather),
                contentScale = FixedScale(0.5f),
                alignment = BiasAlignment(0f, -0.8f),
                contentDescription = null,
                modifier = Modifier.padding(start = 10.dp)
            )
        },
        onClick = { },
        additionalContent = {
            weather?.let { WeatherContent(it) }
        },
        modifier = modifier
    )
}