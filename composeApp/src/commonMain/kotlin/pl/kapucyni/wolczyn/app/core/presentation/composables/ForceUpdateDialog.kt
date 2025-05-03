package pl.kapucyni.wolczyn.app.core.presentation.composables

import SMWolczyn.composeApp.BuildConfig
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.storeLink
import pl.kapucyni.wolczyn.app.core.domain.model.AppConfiguration
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.force_update_dialog_btn
import smwolczyn.composeapp.generated.resources.force_update_dialog_message
import smwolczyn.composeapp.generated.resources.force_update_dialog_title

@Composable
fun ForceUpdateDialog(appConfiguration: AppConfiguration?) {
    val uriHandler = LocalUriHandler.current
    var forceUpdateDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(appConfiguration?.forceUpdate) {
        appConfiguration?.forceUpdate?.let { force ->
            var updateNeeded = false
            BuildConfig.APP_VERSION
                .split(".")
                .zip(force.split("."))
                .map { (version, forceUpdate) ->
                    version.toIntOrNull() to forceUpdate.toIntOrNull()
                }
                .forEach { (version, forceUpdate) ->
                    when {
                        version == null || forceUpdate == null -> return@forEach
                        version < forceUpdate -> {
                            updateNeeded = true
                            return@forEach
                        }
                    }
                }
            forceUpdateDialogVisible = updateNeeded
        }
    }

    WolczynAlertDialog(
        isVisible = forceUpdateDialogVisible,
        imageVector = Icons.Default.NewReleases,
        dialogTitleId = Res.string.force_update_dialog_title,
        dialogTextId = Res.string.force_update_dialog_message,
        confirmBtnTextId = Res.string.force_update_dialog_btn,
        onConfirm = { uriHandler.openUri(storeLink) },
        onDismissRequest = {},
        dismissible = false,
    )
}