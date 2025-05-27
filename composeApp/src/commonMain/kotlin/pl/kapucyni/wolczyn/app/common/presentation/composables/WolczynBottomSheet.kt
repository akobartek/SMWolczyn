package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WolczynBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
) {
    if (isVisible)
        ModalBottomSheet(
            onDismissRequest = onDismiss,
        ) {
            WolczynText("xD")
        }
}