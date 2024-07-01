package pl.kapucyni.wolczyn.app.shop.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.shop.domain.model.ProductColor
import pl.kapucyni.wolczyn.app.shop.domain.model.ProductColorIndicator
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.shop_color_black
import smwolczyn.composeapp.generated.resources.shop_color_brown
import smwolczyn.composeapp.generated.resources.shop_color_green
import smwolczyn.composeapp.generated.resources.shop_color_white

@Composable
fun ProductColorsList(
    colors: Set<ProductColor>,
    selectedColor: ProductColor?,
    onColorSelected: (ProductColor) -> Unit,
    modifier: Modifier
) {
    if (selectedColor != null && selectedColor != ProductColor.NONE)
        Column(modifier = modifier) {
            WolczynText(
                text = stringResource(
                    when (selectedColor) {
                        ProductColor.BLACK -> Res.string.shop_color_black
                        ProductColor.GREEN -> Res.string.shop_color_green
                        ProductColor.BROWN -> Res.string.shop_color_brown
                        ProductColor.WHITE -> Res.string.shop_color_white
                        else -> Res.string.shop_color_black
                    }
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                )
            )
            HeightSpacer(4.dp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                colors.forEach {
                    ProductColorIndicator(
                        productColor = it,
                        isSelected = it == selectedColor,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onColorSelected(it) }
                    )
                }
            }
        }
}