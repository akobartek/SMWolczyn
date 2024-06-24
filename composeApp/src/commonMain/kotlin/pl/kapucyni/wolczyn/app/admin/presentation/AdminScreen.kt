package pl.kapucyni.wolczyn.app.admin.presentation

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.restricted_access

@Composable
fun AdminScreen(
    onBackPressed: () -> Unit,
//    viewModel: ViewModel = koinInject()
) {
    ScreenLayout(
        title = stringResource(Res.string.restricted_access),
        onBackPressed = onBackPressed
    ) {
        AdminScreenContent()
    }
}

@Composable
fun AdminScreenContent() {

}