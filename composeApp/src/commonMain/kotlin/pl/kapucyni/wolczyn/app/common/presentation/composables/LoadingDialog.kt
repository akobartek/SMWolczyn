package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingDialog(visible: Boolean) {
    if (visible)
        Dialog(onDismissRequest = {}) {
            LoadingBox()
        }
}