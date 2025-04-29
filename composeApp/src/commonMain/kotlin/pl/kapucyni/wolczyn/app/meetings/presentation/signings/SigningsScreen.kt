package pl.kapucyni.wolczyn.app.meetings.presentation.signings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.*
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_save_profile
import smwolczyn.composeapp.generated.resources.signings

@Composable
fun SigningsScreen(
    navigateUp: () -> Unit,
    viewModel: SigningsViewModel,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    ScreenLayout(
        title = stringResource(Res.string.signings),
        onBackPressed = navigateUp,
        actionIcon = {
            IconButton(onClick = { viewModel.handleAction(SaveData) }) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    tint = wolczynColors.primary,
                    contentDescription = stringResource(Res.string.cd_save_profile),
                )
            }
        },
    ) {
        when (state) {
            is State.Loading -> LoadingBox()
            is State.Success -> (state as? State.Success)?.let {
                println(it)
            } ?: LoadingBox()
        }
    }
}