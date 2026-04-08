package pl.kapucyni.wolczyn.app.core.presentation.composables

import SMWolczyn.composeApp.BuildConfig
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.LifecycleManager
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.storeLink
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import pl.kapucyni.wolczyn.app.core.domain.model.platformForceUpdate
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.close
import smwolczyn.composeapp.generated.resources.force_update_dialog_btn
import smwolczyn.composeapp.generated.resources.force_update_dialog_message
import smwolczyn.composeapp.generated.resources.force_update_dialog_title
import smwolczyn.composeapp.generated.resources.ic_release_alert

@Composable
fun ForceUpdateDialog(
    appConfiguration: AppConfiguration?,
    lifecycleManager: LifecycleManager = koinInject(),
) {
    val uriHandler = LocalUriHandler.current
    var forceUpdateDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(appConfiguration) {
        appConfiguration?.platformForceUpdate()?.let { forceVersion ->
            forceUpdateDialogVisible = forceVersion > BuildConfig.APP_VERSION
        }
    }

    WolczynAlertDialog(
        isVisible = forceUpdateDialogVisible,
        imageVector = vectorResource(Res.drawable.ic_release_alert),
        dialogTitleId = Res.string.force_update_dialog_title,
        dialogTextId = Res.string.force_update_dialog_message,
        confirmBtnTextId = Res.string.force_update_dialog_btn,
        onConfirm = { uriHandler.openUri(storeLink) },
        onDismissRequest = { lifecycleManager.closeApp() },
        dismissBtnTextId = Res.string.close,
        dismissible = false,
    )
}