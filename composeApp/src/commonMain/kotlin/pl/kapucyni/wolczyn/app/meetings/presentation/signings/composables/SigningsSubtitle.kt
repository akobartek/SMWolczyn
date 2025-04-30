package pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
    val userName = "${state.firstName} ${state.lastName}"
    WolczynText(
        text =
            if (state.isEditing) {
                buildAnnotatedString {
                    append(stringResource(Res.string.signings_subtitle_edit, userName))
                }
            } else {
                buildAnnotatedString {
                    stringResource(Res.string.signings_subtitle, userName)
                        .split(MEETING_TITLE)
                        .let {
                            append(it.first())
                            withStyle(
                                style = SpanStyle(fontStyle = FontStyle.Italic)
                            ) {
                                append(state.meeting.name)
                            }
                            append(it.last())
                        }
                }
            },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Justify,
        ),
        modifier = modifier,
    )
}

private const val MEETING_TITLE = "%meeting_title%"