package pl.kapucyni.wolczyn.app.common.utils

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(true, onBack)
}