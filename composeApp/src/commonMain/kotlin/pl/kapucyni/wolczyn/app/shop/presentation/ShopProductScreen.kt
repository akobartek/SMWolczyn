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
import org.koin.core.annotation.KoinExperimentalAPI
import pl.kapucyni.wolczyn.app.shop.domain.model.ShopProduct
import pl.kapucyni.wolczyn.app.shop.presentation.composables.ProductDetails

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ShopProductScreen(
    product: ShopProduct,
    onBackPressed: () -> Unit,
) {
    ShopProductScreenContent(product, onBackPressed)
}

@Composable
fun ShopProductScreenContent(
    product: ShopProduct,
    onBackPressed: () -> Unit,
) {
    var selectedColor by rememberSaveable {
        mutableStateOf(product.photoUrls.keys.firstOrNull())
    }
    var isOrientationLandscape by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.onGloballyPositioned {
            isOrientationLandscape = it.size.width > it.size.height
        },
    ) {
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