package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingDialog(
    visible: Boolean,
    onDismiss: (() -> Unit)? = null,
) {
    if (visible) {
        var dismissed by remember { mutableStateOf(false) }
        if (dismissed.not())
            Dialog(
                onDismissRequest = {
                    onDismiss?.let {
                        dismissed = true
                        it.invoke()
                    }
                },
            ) {
                LoadingBox()
            }
    }
}