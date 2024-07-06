package pl.kapucyni.wolczyn.app.admin.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import pl.kapucyni.wolczyn.app.admin.presentation.model.AdminScreenAction.*
import pl.kapucyni.wolczyn.app.admin.presentation.composables.AdminDataDialog
import pl.kapucyni.wolczyn.app.admin.presentation.composables.AdminQuizDialog
import pl.kapucyni.wolczyn.app.admin.presentation.model.AdminData
import pl.kapucyni.wolczyn.app.admin.presentation.model.AdminScreenAction
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynTitleText
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizState
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.app_data
import smwolczyn.composeapp.generated.resources.kitchen
import smwolczyn.composeapp.generated.resources.kitchen_menu
import smwolczyn.composeapp.generated.resources.kitchen_menu_title
import smwolczyn.composeapp.generated.resources.kitchen_promos_title
import smwolczyn.composeapp.generated.resources.kitchen_quiz_title
import smwolczyn.composeapp.generated.resources.promotions
import smwolczyn.composeapp.generated.resources.quiz
import smwolczyn.composeapp.generated.resources.shop
import smwolczyn.composeapp.generated.resources.shop_products
import smwolczyn.composeapp.generated.resources.shop_products_title
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
    var isKitchenMenuDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isKitchenPromosDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isKitchenQuizDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isShopProductsDialogVisible by rememberSaveable { mutableStateOf(false) }
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
                onClick = { isKitchenMenuDialogVisible = true },
                modifier = Modifier.weight(1f),
            ) {
                WolczynText(stringResource(Res.string.kitchen_menu))
            }

            Button(
                onClick = { isKitchenPromosDialogVisible = true },
                modifier = Modifier.weight(1f),
            ) {
                WolczynText(stringResource(Res.string.promotions))
            }

            Button(
                onClick = { isKitchenQuizDialogVisible = true },
                enabled = data.kitchenQuiz?.let { it.state != QuizState.NOT_AVAILABLE } ?: false,
                modifier = Modifier.weight(1f),
            ) {
                WolczynText(stringResource(Res.string.quiz))
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
                onClick = { isShopProductsDialogVisible = true },
                modifier = Modifier.weight(1f),
            ) {
                WolczynText(stringResource(Res.string.shop_products))
            }

            Button(
                onClick = { isShopPromosDialogVisible = true },
                modifier = Modifier.weight(1f),
            ) {
                WolczynText(stringResource(Res.string.promotions))
            }
        }
    }

    AdminDataDialog(
        isVisible = isKitchenMenuDialogVisible,
        title = stringResource(Res.string.kitchen_menu_title),
        data = data.kitchenMenuItems.map { AdminData.fromMenuItem(it) },
        onPromotionAdd = null,
        onPromotionUpdate = { id, checked ->
            data.kitchenMenuItems.firstOrNull { it.id == id }?.let { item ->
                handleScreenAction(UpdateMenuItem(item.copy(isAvailable = checked)))
            }
        },
        onPromotionDelete = null,
        onDismiss = { isKitchenMenuDialogVisible = false },
    )

    AdminDataDialog(
        isVisible = isKitchenPromosDialogVisible,
        title = stringResource(Res.string.kitchen_promos_title),
        data = data.kitchenPromotions.map { AdminData.fromPromotion(it) },
        onPromotionAdd = { handleScreenAction(AddPromotion(it, true)) },
        onPromotionUpdate = { id, checked ->
            data.kitchenPromotions.firstOrNull { it.id == id }?.let { promo ->
                handleScreenAction(UpdatePromotion(promo.copy(isValid = checked), true))
            }
        },
        onPromotionDelete = { handleScreenAction(DeletePromotion(it, true)) },
        onDismiss = { isKitchenPromosDialogVisible = false },
    )

    data.kitchenQuiz?.let { quiz ->
        AdminQuizDialog(
            isVisible = isKitchenQuizDialogVisible,
            title = stringResource(Res.string.kitchen_quiz_title),
            quiz = quiz,
            quizResults = data.kitchenQuizResults,
            onQuizStateChanged = { newState -> handleScreenAction(UpdateQuiz(quiz, newState)) },
            onDismiss = { isKitchenQuizDialogVisible = false }
        )
    }

    AdminDataDialog(
        isVisible = isShopProductsDialogVisible,
        title = stringResource(Res.string.shop_products_title),
        data = data.shopProducts.map { AdminData.fromShopProduct(it) },
        onPromotionAdd = null,
        onPromotionUpdate = { id, checked ->
            data.shopProducts.firstOrNull { it.id == id }?.let { item ->
                handleScreenAction(UpdateShopProduct(item.copy(isAvailable = checked)))
            }
        },
        onPromotionDelete = null,
        onDismiss = { isShopProductsDialogVisible = false },
    )

    AdminDataDialog(
        isVisible = isShopPromosDialogVisible,
        title = stringResource(Res.string.shop_promos_title),
        data = data.shopPromotions.map { AdminData.fromPromotion(it) },
        onPromotionAdd = { handleScreenAction(AddPromotion(it, false)) },
        onPromotionUpdate = { id, checked ->
            data.shopPromotions.firstOrNull { it.id == id }?.let { promo ->
                handleScreenAction(UpdatePromotion(promo.copy(isValid = checked), false))
            }
        },
        onPromotionDelete = { handleScreenAction(DeletePromotion(it, false)) },
        onDismiss = { isShopPromosDialogVisible = false },
    )
}