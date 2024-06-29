package pl.kapucyni.wolczyn.app.kitchen.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.PromotionBar
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynTitleText
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenu
import pl.kapucyni.wolczyn.app.kitchen.domain.model.KitchenMenuSection
import pl.kapucyni.wolczyn.app.kitchen.presentation.composables.KitchenMenuItem
import pl.kapucyni.wolczyn.app.kitchen.presentation.composables.KitchenSectionHeader
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_kitchen_beverages
import smwolczyn.composeapp.generated.resources.ic_kitchen_snacks
import smwolczyn.composeapp.generated.resources.ic_kitchen_sweets
import smwolczyn.composeapp.generated.resources.kitchen_menu
import smwolczyn.composeapp.generated.resources.kitchen_section_beverages
import smwolczyn.composeapp.generated.resources.kitchen_section_snacks
import smwolczyn.composeapp.generated.resources.kitchen_section_sweets
import smwolczyn.composeapp.generated.resources.kitchen_title

@Composable
fun KitchenScreen(
    onBackPressed: () -> Unit,
    viewModel: KitchenViewModel = koinInject()
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()
    val openPromotions by viewModel.openPromotions.collectAsStateMultiplatform()

    ScreenLayout(
        title = stringResource(Res.string.kitchen_title),
        onBackPressed = onBackPressed
    ) {
        KitchenScreenContent(
            screenState = screenState,
            openPromotions = openPromotions,
            onPromotionRemove = viewModel::removePromotion
        )
    }
}

@Composable
fun KitchenScreenContent(
    screenState: State<KitchenMenu>,
    openPromotions: List<String>,
    onPromotionRemove: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp)
    ) {
        items(items = openPromotions, key = { it }) { promo ->
            PromotionBar(
                name = promo,
                onRemove = onPromotionRemove
            )
        }
        if (openPromotions.isNotEmpty())
            item { HeightSpacer(12.dp) }

        item {
            WolczynTitleText(
                text = stringResource(Res.string.kitchen_menu),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        when (screenState) {
            is State.Loading -> item { LoadingBox() }
            is State.Success -> {
                screenState.data.menu.forEach { (section, menuItems) ->
                    item {
                        KitchenSectionHeader(
                            title = stringResource(
                                when (section) {
                                    KitchenMenuSection.SNACKS -> Res.string.kitchen_section_snacks
                                    KitchenMenuSection.SWEETS -> Res.string.kitchen_section_sweets
                                    KitchenMenuSection.BEVERAGES -> Res.string.kitchen_section_beverages
                                }
                            ),
                            icon = painterResource(
                                when (section) {
                                    KitchenMenuSection.SNACKS -> Res.drawable.ic_kitchen_snacks
                                    KitchenMenuSection.SWEETS -> Res.drawable.ic_kitchen_sweets
                                    KitchenMenuSection.BEVERAGES -> Res.drawable.ic_kitchen_beverages
                                }
                            )
                        )
                    }
                    items(items = menuItems, key = { it.id }) { item ->
                        KitchenMenuItem(item = item)
                    }
                }
            }
        }
    }
}