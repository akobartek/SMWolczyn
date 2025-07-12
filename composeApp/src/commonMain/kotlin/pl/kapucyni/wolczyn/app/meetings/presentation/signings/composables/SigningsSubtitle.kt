package pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsScreenState
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.signings_subtitle
import smwolczyn.composeapp.generated.resources.signings_subtitle_edit

@Composable
fun SigningsSubtitle(
    modifier: Modifier = Modifier,
    state: SigningsScreenState,
) {
    WolczynText(
        text = stringResource(
            if (state.isEditing) Res.string.signings_subtitle_edit
            else Res.string.signings_subtitle,
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Justify),
        modifier = modifier,
    )
}