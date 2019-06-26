package pl.kapucyni.wolczyn.app.model

data class Weather(
    val list: List<WeatherRecord> = listOf()
)

data class WeatherRecord(
    val main: WeatherMain = WeatherMain(),
    val weather: List<WeatherCodes> = listOf(),
    var dt_txt: String = ""
)

data class WeatherMain(
    val temp: Double = 0.0
)

data class WeatherCodes(
    val icon: String = ""
)