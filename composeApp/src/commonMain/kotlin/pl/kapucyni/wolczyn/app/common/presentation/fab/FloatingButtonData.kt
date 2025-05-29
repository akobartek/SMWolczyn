package pl.kapucyni.wolczyn.app.common.presentation.fab

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

data class FloatingButtonData(
    val icon: ImageVector,
    val contentDescription: StringResource,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val isSmall: Boolean = false,
)
