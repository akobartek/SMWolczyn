package pl.kapucyni.wolczyn.app.shop.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.presentation.composables.ProductColorsList
import pl.kapucyni.wolczyn.app.shop.presentation.composables.ProductPhotosPager
import pl.kapucyni.wolczyn.app.shop.presentation.composables.ProductSizes

@Composable
fun ShopProductScreen(
    productId: String?,
    onBackPressed: () -> Unit,
    viewModel: ShopViewModel = koinInject(),
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()
    ShopProductScreenContent(productId, screenState, onBackPressed)
}

@Composable
fun ShopProductScreenContent(
    productId: String?,
    screenState: BasicViewModel.State<Shop>,
    onBackPressed: () -> Unit,
) {
    when (screenState) {
        is BasicViewModel.State.Loading -> LoadingBox()
        is BasicViewModel.State.Success -> {
            val product = screenState.data.products.find { it.id == productId }
            if (product == null || productId == null) {
                onBackPressed()
                return
            }
            var selectedColor by rememberSaveable {
                mutableStateOf(product.photosUrls.keys.firstOrNull())
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                ProductPhotosPager(
                    photos = listOf("1", "2", "3", "4", "5"),
                    onBackPressed = onBackPressed
                )

                Spacer(modifier = Modifier.height(12.dp))

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
                        onColorSelected = { selectedColor = it }
                    )
                    ProductSizes(sizes = product.sizes)
                }
            }
        }
    }
}