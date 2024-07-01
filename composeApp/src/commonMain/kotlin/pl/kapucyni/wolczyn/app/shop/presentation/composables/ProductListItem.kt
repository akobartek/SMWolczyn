package pl.kapucyni.wolczyn.app.shop.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.shop.domain.model.ShopProduct
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.wolczyn_logo

@Composable
fun ProductListItem(
    product: ShopProduct,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        val photoModifier = Modifier.fillMaxWidth().height(220.dp)
        product.getMainPhotoUrl()?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(Res.drawable.wolczyn_logo),
                modifier = photoModifier,
            )
        } ?: ProductEmptyPhoto(modifier = photoModifier)

        HeightSpacer(4.dp)

        WolczynText(
            text = product.name,
            textStyle = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),
            isOneLiner = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}