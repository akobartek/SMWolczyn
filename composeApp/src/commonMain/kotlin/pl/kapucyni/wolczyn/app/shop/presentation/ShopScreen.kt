package pl.kapucyni.wolczyn.app.shop.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.presentation.composables.ProductListItem
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.shop_title

@Composable
fun ShopScreen(
    onBackPressed: () -> Unit,
    onProductClick: (String) -> Unit,
    viewModel: ShopViewModel = koinInject(),
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()

    ScreenLayout(
        title = stringResource(Res.string.shop_title),
        onBackPressed = onBackPressed
    ) {
        ShopScreenContent(screenState, onProductClick)
    }
}

@Composable
fun ShopScreenContent(
    screenState: State<Shop>,
    onProductClick: (String) -> Unit,
) {
    when (screenState) {
        is State.Loading -> LoadingBox()
        is State.Success -> {
            val shop = screenState.data
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp)
            ) {
                items(
                    count = shop.products.size,
                    key = { shop.products[it].id }
                ) { index ->
                    val product = shop.products[index]
                    ProductListItem(
                        product = product,
                        modifier = Modifier.clickable { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}
