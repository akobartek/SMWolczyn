package pl.kapucyni.wolczyn.app.admin.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.admin.data.model.FirestoreData
import pl.kapucyni.wolczyn.app.admin.presentation.AdminScreenAction.*
import pl.kapucyni.wolczyn.app.admin.presentation.composables.PromotionsDialog
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynTitleText
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.app_data
import smwolczyn.composeapp.generated.resources.kitchen
import smwolczyn.composeapp.generated.resources.kitchen_menu
import smwolczyn.composeapp.generated.resources.kitchen_promos_title
import smwolczyn.composeapp.generated.resources.promotions
import smwolczyn.composeapp.generated.resources.quiz
import smwolczyn.composeapp.generated.resources.shop
import smwolczyn.composeapp.generated.resources.shop_products
import smwolczyn.composeapp.generated.resources.shop_promos_title

@Composable
fun AdminScreen(
    onBackPressed: () -> Unit,
    viewModel: AdminViewModel = koinInject()
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()

    ScreenLayout(
        title = stringResource(Res.string.app_data),
        onBackPressed = onBackPressed
    ) {
        if (screenState is State.Success<FirestoreData>)
            AdminScreenContent(
                data = (screenState as State.Success<FirestoreData>).data,
                handleScreenAction = viewModel::handleScreenAction
            )
        else LoadingBox()
    }
}

@Composable
fun AdminScreenContent(
    data: FirestoreData,
    handleScreenAction: (AdminScreenAction) -> Unit
) {
    var isKitchenPromosDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isShopPromosDialogVisible by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        WolczynTitleText(
            text = stringResource(Res.string.kitchen),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Button(
                onClick = { /*TODO*/ },
                enabled = false,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(Res.string.kitchen_menu))
            }

            Button(
                onClick = { isKitchenPromosDialogVisible = true },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(Res.string.promotions))
            }

            Button(
                onClick = { /*TODO*/ },
                enabled = false,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(Res.string.quiz))
            }
        }

        WolczynTitleText(
            text = stringResource(Res.string.shop),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
        ) {
            Button(
                onClick = { /*TODO*/ },
                enabled = false,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(Res.string.shop_products))
            }

            Button(
                onClick = { isShopPromosDialogVisible = true },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(Res.string.promotions))
            }
        }
    }

    PromotionsDialog(
        isVisible = isKitchenPromosDialogVisible,
        title = stringResource(Res.string.kitchen_promos_title),
        promotions = data.kitchenPromotions,
        onPromotionAdd = { handleScreenAction(AddPromotion(it, true)) },
        onPromotionUpdate = { handleScreenAction(UpdatePromotion(it, true)) },
        onPromotionDelete = { handleScreenAction(DeletePromotion(it, true)) },
        onDismiss = { isKitchenPromosDialogVisible = false },
    )

    PromotionsDialog(
        isVisible = isShopPromosDialogVisible,
        title = stringResource(Res.string.shop_promos_title),
        promotions = data.shopPromotions,
        onPromotionAdd = { handleScreenAction(AddPromotion(it, false)) },
        onPromotionUpdate = { handleScreenAction(UpdatePromotion(it, false)) },
        onPromotionDelete = { handleScreenAction(DeletePromotion(it, false)) },
        onDismiss = { isShopPromosDialogVisible = false },
    )
}