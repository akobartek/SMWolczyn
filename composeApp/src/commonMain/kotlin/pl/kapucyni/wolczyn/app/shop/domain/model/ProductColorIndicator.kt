package pl.kapucyni.wolczyn.app.shop.domain.model

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.theme.appColorPrimary
import pl.kapucyni.wolczyn.app.theme.shopColorBlack
import pl.kapucyni.wolczyn.app.theme.shopColorBrown
import pl.kapucyni.wolczyn.app.theme.shopColorGreen
import pl.kapucyni.wolczyn.app.theme.shopColorWhite

@Composable
fun ProductColorIndicator(
    productColor: ProductColor,
    isSelected: Boolean,
    modifier: Modifier
) {
    val color = when (productColor) {
        ProductColor.BLACK -> shopColorBlack
        ProductColor.GREEN -> shopColorGreen
        ProductColor.BROWN -> shopColorBrown
        else -> shopColorWhite
    }

    Canvas(modifier = modifier.size(32.dp)) {
        drawCircle(
            color = appColorPrimary,
            radius = (12.5).dp.toPx(),
            center = Offset(x = size.width / 2, y = size.height / 2)
        )
        drawCircle(
            color = color,
            radius = 12.dp.toPx(),
            center = Offset(x = size.width / 2, y = size.height / 2)
        )
        if (isSelected)
            drawCircle(
                color = color,
                radius = 16.dp.toPx(),
                center = Offset(x = size.width / 2, y = size.height / 2),
                style = Stroke(width = 2.dp.toPx())
            )
    }
}