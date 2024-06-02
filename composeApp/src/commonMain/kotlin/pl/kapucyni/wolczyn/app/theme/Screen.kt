package pl.kapucyni.wolczyn.app.theme

sealed class Screen(val route: String) {
    data object Home: Screen("home")
}