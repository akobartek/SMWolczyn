package pl.kapucyni.wolczyn.app.quiz.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.NotificationBar
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.quiz_available

@Composable
fun QuizNotificationBar(
    onStartQuiz: () -> Unit,
    modifier: Modifier,
) {
    NotificationBar(
        name = stringResource(Res.string.quiz_available),
        onIconClick = onStartQuiz,
        modifier = modifier
    )
}