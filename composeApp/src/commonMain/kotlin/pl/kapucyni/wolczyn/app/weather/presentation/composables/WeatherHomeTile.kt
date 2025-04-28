package pl.kapucyni.wolczyn.app.weather.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.kapucyni.wolczyn.app.common.presentation.composables.HomeTile
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather
import pl.kapucyni.wolczyn.app.weather.presentation.WeatherViewModel
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_cap_weather
import smwolczyn.composeapp.generated.resources.weather_title

@OptIn(KoinExperimentalAPI::class)
@Composable
fun WeatherHomeTile(
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = koinViewModel()
) {
    val weather by weatherViewModel.screenState.collectAsStateWithLifecycle()
    WeatherHomeTileContent(
        backgroundColor = backgroundColor,
        weather = weather,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun WeatherHomeTileContent(
    backgroundColor: Color,
    weather: Weather?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var counter by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(counter) {
        when (counter) {
            0 -> return@LaunchedEffect
            5 -> {
                counter = 0
                onClick()
            }
            else -> {
                delay(500)
                counter = 0
            }
        }
    }

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
        onClick = { counter++ },
        additionalContent = {
            weather?.let { WeatherContent(it) }
        },
        modifier = modifier
    )
}