package pl.kapucyni.wolczyn.app.shop.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmptyListInfo
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.PromotionBar
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.shop.domain.model.Shop
import pl.kapucyni.wolczyn.app.shop.presentation.composables.ProductListItem
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.empty_shop_list
import smwolczyn.composeapp.generated.resources.ic_cap_shop
import smwolczyn.composeapp.generated.resources.shop_title

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ShopScreen(
    onBackPressed: () -> Unit,
    onProductClick: (String) -> Unit,
    viewModel: ShopViewModel = koinViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

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
                    count = shop.promotions.size,
                    key = { it },
                    span = { GridItemSpan(maxLineSpan) },
                ) { index ->
                    val promo = shop.promotions[index]
                    PromotionBar(
                        name = promo,
                        onRemove = null,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }

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

                if (shop.products.isEmpty())
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        EmptyListInfo(
                            messageRes = Res.string.empty_shop_list,
                            drawableRes = Res.drawable.ic_cap_shop
                        )
                    }
            }
        }
    }
}
