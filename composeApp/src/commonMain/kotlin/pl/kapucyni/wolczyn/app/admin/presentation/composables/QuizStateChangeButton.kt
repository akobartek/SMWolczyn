package pl.kapucyni.wolczyn.app.admin.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizState
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.quiz_state_finished
import smwolczyn.composeapp.generated.resources.quiz_state_ongoing
import smwolczyn.composeapp.generated.resources.quiz_state_waiting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizStateChangeButton(
    currentState: QuizState,
    onStateChange: (QuizState) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        listOf(
            ButtonData(
                QuizState.WAITING,
                Res.string.quiz_state_waiting,
            ),
            ButtonData(
                QuizState.ONGOING,
                Res.string.quiz_state_ongoing,
            ),
            ButtonData(
                QuizState.FINISHED,
                Res.string.quiz_state_finished,
            ),
        ).forEachIndexed { index, buttonData ->
            val isSelected = currentState == buttonData.quizState
            SegmentedButton(
                selected = isSelected,
                onClick = {
                    if (!isSelected) onStateChange(buttonData.quizState)
                },
                icon = {
                    if (isSelected)
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = 3
                )
            ) {
                WolczynText(stringResource(buttonData.label))
            }
        }
    }
}

private data class ButtonData(
    val quizState: QuizState,
    val label: StringResource,
)