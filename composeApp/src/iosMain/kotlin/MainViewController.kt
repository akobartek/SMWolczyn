import androidx.compose.ui.window.ComposeUIViewController
import pl.kapucyni.wolczyn.app.common.utils.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }