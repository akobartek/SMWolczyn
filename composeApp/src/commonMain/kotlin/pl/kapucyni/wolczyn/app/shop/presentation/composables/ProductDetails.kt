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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    ProductPhotosPager(
        photos = listOf("1", "2", "3", "4", "5"),
        onBackPressed = onBackPressed,
        modifier =
        if (isOrientationLandscape) Modifier.fillMaxHeight().aspectRatio(3 / 4f)
        else Modifier.fillMaxWidth()
    )

    Spacer(
        modifier =
        if (isOrientationLandscape) Modifier.width(12.dp)
        else Modifier.height(12.dp)
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        WolczynText(
            text = product.name,
            textStyle = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            ),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            ProductColorsList(
                colors = product.photosUrls.keys,
                selectedColor = selectedColor,
                onColorSelected = onColorSelected
            )
            ProductSizes(sizes = product.sizes)
        }
    }
}