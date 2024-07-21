package pl.kapucyni.wolczyn.app.shop.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.presentation.composables.ProductDetails

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
    screenState: State<Shop>,
    onBackPressed: () -> Unit,
) {
    when (screenState) {
        is State.Loading -> LoadingBox()
        is State.Success -> {
            val product = screenState.data.products.find { it.id == productId }
            if (product == null || productId == null) {
                onBackPressed()
                return
            }
            var selectedColor by rememberSaveable {
                mutableStateOf(product.photoUrls.keys.firstOrNull())
            }
            var isOrientationLandscape by remember { mutableStateOf(false) }

            Box(modifier = Modifier.onGloballyPositioned {
                isOrientationLandscape = it.size.width > it.size.height
            }) {
                val details = @Composable {
                    ProductDetails(
                        product = product,
                        selectedColor = selectedColor,
                        onColorSelected = { selectedColor = it },
                        onBackPressed = onBackPressed,
                        isOrientationLandscape = isOrientationLandscape
                    )
                }

                if (!isOrientationLandscape)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        details()
                    }
                else
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        details()
                    }
            }
        }
    }
}