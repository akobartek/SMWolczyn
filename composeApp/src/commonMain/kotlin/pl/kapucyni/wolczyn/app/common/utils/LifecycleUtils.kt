package pl.kapucyni.wolczyn.app.common.utils

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(onBack: () -> Unit)