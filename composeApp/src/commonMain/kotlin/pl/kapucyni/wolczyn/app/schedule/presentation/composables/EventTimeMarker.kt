package pl.kapucyni.wolczyn.app.schedule.presentation.composables

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun EventTimeMarker(
    filledCircle: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        drawCircle(
            color = color,
            radius = 6.dp.toPx(),
            style = if (filledCircle) Fill else Stroke(width = 2.dp.toPx()),
            center = Offset.Zero
        )
        if (!isLast)
            drawLine(
                color = color,
                start = Offset(x = 0f, y = 14.dp.toPx()),
                end = Offset(x = 0f, y = size.height),
                strokeWidth = 2.dp.toPx()
            )
    }
}