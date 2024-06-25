package pl.kapucyni.wolczyn.app.admin.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.app_data

@Composable
fun AdminScreen(
    onBackPressed: () -> Unit,
    viewModel: AdminViewModel = koinInject()
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()

    ScreenLayout(
        title = stringResource(Res.string.app_data),
        onBackPressed = onBackPressed
    ) {
        AdminScreenContent()
    }
}

@Composable
fun AdminScreenContent() {

}