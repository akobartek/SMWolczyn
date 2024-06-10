package pl.kapucyni.wolczyn.app.shop.presentation

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop

@Composable
fun ShopProductScreen(
    productId: String,
    onBackPressed: () -> Unit,
    viewModel: ShopViewModel = koinInject(),
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()
    ShopProductScreenContent(productId, screenState, onBackPressed)
}

@Composable
fun ShopProductScreenContent(
    productId: String,
    screenState: BasicViewModel.State<Shop>,
    onBackPressed: () -> Unit,
) {
    when (screenState) {
        is BasicViewModel.State.Loading -> LoadingBox()
        is BasicViewModel.State.Success -> {
            val product = screenState.data.products.find { it.id == productId }
            if (product == null) {
                onBackPressed()
                return
            }
            Text(product.name, modifier = Modifier.clickable { onBackPressed() })
        }
    }
}