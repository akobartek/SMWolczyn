package pl.kapucyni.wolczyn.app.shop.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.shop.domain.model.ProductColor
import pl.kapucyni.wolczyn.app.shop.domain.model.ShopProduct

@Composable
fun ProductDetails(
    product: ShopProduct,
    selectedColor: ProductColor?,
    onColorSelected: (ProductColor) -> Unit,
    onBackPressed: () -> Unit,
    isOrientationLandscape: Boolean
) {
    val modifier =
        if (isOrientationLandscape) Modifier.fillMaxHeight().aspectRatio(3 / 4f)
        else Modifier.fillMaxWidth()
    product.photoUrls[selectedColor]?.let { urls ->
        ProductPhotosPager(
            photos = urls,
            onBackPressed = onBackPressed,
            modifier = modifier
        )
    } ?: ProductEmptyPhoto(modifier = modifier)

    Spacer(
        modifier =
        if (isOrientationLandscape) Modifier.width(12.dp)
        else Modifier.height(12.dp)
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        WolczynText(
            text = product.name,
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        HeightSpacer(24.dp)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            ProductColorsList(
                colors = product.photoUrls.keys.filter { it != ProductColor.NONE }.toSet(),
                selectedColor = selectedColor,
                onColorSelected = onColorSelected,
                modifier = Modifier.weight(1f),
            )
            ProductSizes(
                sizes = product.sizes,
                modifier = Modifier.weight(1f),
            )
        }
    }
}