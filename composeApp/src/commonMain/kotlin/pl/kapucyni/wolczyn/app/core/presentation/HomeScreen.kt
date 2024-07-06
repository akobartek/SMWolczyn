package pl.kapucyni.wolczyn.app.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType
import pl.kapucyni.wolczyn.app.common.presentation.composables.NotificationBar
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.core.domain.model.AppState
import pl.kapucyni.wolczyn.app.core.domain.model.AppVersion
import pl.kapucyni.wolczyn.app.core.presentation.composables.AdminAccessDialog
import pl.kapucyni.wolczyn.app.core.presentation.composables.AuthDialog
import pl.kapucyni.wolczyn.app.core.presentation.composables.HomeTileList
import pl.kapucyni.wolczyn.app.core.presentation.model.AuthDialogState.AuthSnackBarType.*
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_navigate_up
import smwolczyn.composeapp.generated.resources.empty
import smwolczyn.composeapp.generated.resources.home_title
import smwolczyn.composeapp.generated.resources.signed_in
import smwolczyn.composeapp.generated.resources.signed_out

@Composable
fun HomeScreen(
    onTileClick: (HomeTileType) -> Unit,
    viewModel: HomeScreenViewModel = koinInject(),
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()
    val authDialogState by viewModel.authState.collectAsStateMultiplatform()
    viewModel.checkDialog()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarMessage = stringResource(
        when (authDialogState.snackbarType) {
            SIGNED_IN -> Res.string.signed_in
            SIGNED_OUT -> Res.string.signed_out
            else -> Res.string.empty
        }
    )

    LaunchedEffect(authDialogState) {
        authDialogState.snackbarType?.let {
            val result = snackbarHostState.showSnackbar(
                message = snackBarMessage,
                withDismissAction = true,
            )
            if (result == SnackbarResult.Dismissed)
                viewModel.clearSnackBar()
        } ?: snackbarHostState.currentSnackbarData?.dismiss()
    }

    ScreenLayout(
        title = stringResource(Res.string.home_title),
        actionIcon = {
            IconButton(onClick = viewModel::showAuthDialog) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    tint = wolczynColors.primary,
                    contentDescription = stringResource(Res.string.cd_navigate_up)
                )
            }
        },
        snackbarHostState = snackbarHostState,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        HomeScreenContent(
            state = screenState,
            onTileClick = onTileClick
        )
        AuthDialog(
            state = authDialogState,
            onSignIn = viewModel::signIn,
            onSignOut = viewModel::signOut,
            onDismiss = viewModel::hideAuthDialog,
            loadGroupInfo = viewModel::loadGroupInfo,
        )
    }
}

@Composable
fun HomeScreenContent(
    state: State<AppState>,
    onTileClick: (HomeTileType) -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val appVersion =
        if (state is State.Success) state.data.configuration.appVersion
        else AppVersion.MEETING

    var screenWidth by remember { mutableStateOf(0) }
    val screenWidthDp = with(LocalDensity.current) { screenWidth.toDp() }
    val columns = (screenWidthDp.value / 360).toInt().coerceIn(1, 3)
    var adminAccessDialogVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .onGloballyPositioned { screenWidth = it.size.width }
            .padding(horizontal = 20.dp)
    ) {
        if (state is State.Success)
            state.data.notifications.forEach { notification ->
                if (notification.message.isNotBlank())
                    NotificationBar(
                        name = notification.message,
                        onIconClick = notification.url.takeIf { it.isNotBlank() }
                            ?.let { url -> { uriHandler.openUri(url) } }
                    )
            }

        HomeTileList(
            columns = columns,
            appVersion = appVersion,
            onTileClick = {
                when (it) {
                    HomeTileType.WEATHER -> adminAccessDialogVisible = true
                    else -> onTileClick(it)
                }
            },
        )
    }

    AdminAccessDialog(
        isVisible = adminAccessDialogVisible,
        onAccess = {
            adminAccessDialogVisible = false
            onTileClick(HomeTileType.WEATHER)
        },
        onCancel = { adminAccessDialogVisible = false }
    )
}