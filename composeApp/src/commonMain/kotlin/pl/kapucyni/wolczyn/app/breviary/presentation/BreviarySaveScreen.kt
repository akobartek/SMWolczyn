package pl.kapucyni.wolczyn.app.breviary.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.kapucyni.wolczyn.app.breviary.presentation.BreviarySaveViewModel.State
import pl.kapucyni.wolczyn.app.breviary.presentation.composables.DownloadLayout
import pl.kapucyni.wolczyn.app.breviary.presentation.composables.MultipleOfficesDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.ok
import smwolczyn.composeapp.generated.resources.save
import smwolczyn.composeapp.generated.resources.save_breviary_dialog_msg
import smwolczyn.composeapp.generated.resources.save_finished_dialog_msg
import smwolczyn.composeapp.generated.resources.saving_breviary
import smwolczyn.composeapp.generated.resources.stop
import smwolczyn.composeapp.generated.resources.stop_action_title
import smwolczyn.composeapp.generated.resources.stop_download_dialog_msg

@OptIn(KoinExperimentalAPI::class)
@Composable
fun BreviarySaveScreen(
    onBackPressed: () -> Unit,
    date: String,
    viewModel: BreviarySaveViewModel = koinViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    var exitDialogVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.setup(date)
    }

    LaunchedEffect(key1 = screenState) {
        val currentState = screenState
        if (currentState is State.Downloading && currentState.breviaryDay.id > 0)
            exitDialogVisible = false
    }

    val handleBackPress = {
        val currentState = screenState
        if (currentState is State.Downloading && currentState.breviaryDay.id == 0L)
            exitDialogVisible = true
        else onBackPressed()
    }

    ScreenLayout(
        title = stringResource(Res.string.saving_breviary),
        onBackPressed = handleBackPress
    ) {
        BreviarySaveScreenContent(
            state = screenState,
            onBackPressed = handleBackPress,
            onCheckOffices = viewModel::checkIfThereAreMultipleOffices,
            onOfficeSelected = viewModel::officeSelected,
            onCancelScreen = viewModel::cancelScreen,
        )
    }

    WolczynAlertDialog(
        isVisible = exitDialogVisible,
        imageVector = Icons.Default.ErrorOutline,
        dialogTitleId = Res.string.stop_action_title,
        dialogTextId = Res.string.stop_download_dialog_msg,
        dismissible = false,
        confirmBtnTextId = Res.string.stop,
        dismissBtnTextId = Res.string.cancel,
        onConfirm = {
            exitDialogVisible = false
            onBackPressed()
        },
        onDismissRequest = { exitDialogVisible = false }
    )
}

@Composable
fun BreviarySaveScreenContent(
    state: State,
    onBackPressed: () -> Unit,
    onCheckOffices: () -> Unit,
    onOfficeSelected: (String) -> Unit,
    onCancelScreen: () -> Unit,
) {
    var saveCompleteDialogVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = state) {
        if (state is State.Downloading && state.breviaryDay.id > 0)
            saveCompleteDialogVisible = true
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        when (state) {
            is State.Cancelled -> onBackPressed()

            is State.Loading -> LoadingBox()

            is State.Init -> WolczynAlertDialog(
                isVisible = true,
                imageVector = Icons.Default.Save,
                dialogTitleId = Res.string.saving_breviary,
                dialogTextId = Res.string.save_breviary_dialog_msg,
                dismissible = false,
                confirmBtnTextId = Res.string.save,
                onConfirm = onCheckOffices,
                dismissBtnTextId = Res.string.cancel,
                onDismissRequest = onCancelScreen
            )

            is State.MultipleOffices -> MultipleOfficesDialog(
                offices = state.offices,
                onSelect = onOfficeSelected,
                onCancel = onCancelScreen
            )

            is State.Downloading -> DownloadLayout(
                breviaryDay = state.breviaryDay,
                onBackPressed = onBackPressed
            )

            is State.Failure -> NoInternetDialog(
                isVisible = true,
                onReconnect = onCheckOffices,
                onDismiss = onCancelScreen
            )
        }

        WolczynAlertDialog(
            isVisible = saveCompleteDialogVisible,
            imageVector = Icons.Default.Save,
            dialogTitleId = Res.string.saving_breviary,
            dialogTextId = Res.string.save_finished_dialog_msg,
            confirmBtnTextId = Res.string.ok,
            onConfirm = { saveCompleteDialogVisible = false },
            onDismissRequest = { saveCompleteDialogVisible = false }
        )
    }
}