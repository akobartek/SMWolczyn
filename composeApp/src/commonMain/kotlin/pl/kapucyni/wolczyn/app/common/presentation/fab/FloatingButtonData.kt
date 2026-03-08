package pl.kapucyni.wolczyn.app.common.presentation.fab

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class FloatingButtonData(
    val icon: DrawableResource,
    val contentDescription: StringResource,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val isSmall: Boolean = false,
)
