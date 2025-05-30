package pl.kapucyni.wolczyn.app.common.presentation.snackbars

import org.jetbrains.compose.resources.StringResource

data class SnackbarAction(
    val name: StringResource,
    val action: suspend () -> Unit,
)
