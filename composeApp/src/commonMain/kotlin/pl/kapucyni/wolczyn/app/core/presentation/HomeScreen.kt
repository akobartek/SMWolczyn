package pl.kapucyni.wolczyn.app.core.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.HomeTileType
import pl.kapucyni.wolczyn.app.common.presentation.composables.NotificationBar
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.core.domain.model.AppState
import pl.kapucyni.wolczyn.app.core.domain.model.AppVersion
import pl.kapucyni.wolczyn.app.core.presentation.composables.AdminAccessDialog
import pl.kapucyni.wolczyn.app.core.presentation.composables.HomeTileList
import pl.kapucyni.wolczyn.app.core.presentation.composables.ProfileOptions
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.home_title

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    user: User?,
    openSignIn: () -> Unit,
    handleAuthAction: (AuthAction) -> Unit,
    onTileClick: (HomeTileType) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    ScreenLayout(
        title = stringResource(Res.string.home_title)
                + (user?.firstName?.let { ",\n$it!" } ?: "!"),
        actionIcon = {
            ProfileOptions(
                user = user,
                openSignIn = openSignIn,
                handleAuthAction = handleAuthAction,
            )
        },
        modifier = Modifier
            .animateContentSize()
            .verticalScroll(rememberScrollState())
    ) {
        HomeScreenContent(
            state = screenState,
            onTileClick = onTileClick
        )
    }
}

@Composable
private fun HomeScreenContent(
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
                        onClick = notification.url.takeIf { it.isNotBlank() }
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