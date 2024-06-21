package pl.kapucyni.wolczyn.app.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.kapucyni.wolczyn.app.weather.domain.model.Weather
import pl.kapucyni.wolczyn.app.weather.domain.usecases.GetWeatherUseCase

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<Weather?>(null)
    val screenState: StateFlow<Weather?> = _screenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getWeatherUseCase()
                    .shareIn(this, SharingStarted.Lazily, 1)
                    .collect { weather -> _screenState.update { weather } }
            } catch (_: Exception) {
            }
        }
    }
}