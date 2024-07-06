package pl.kapucyni.wolczyn.app.admin.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.FullScreenDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.quiz.data.model.FirestoreQuiz
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizResult
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizState
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.quiz_results
import smwolczyn.composeapp.generated.resources.quiz_results_ongoing_message

@Composable
fun AdminQuizDialog(
    isVisible: Boolean,
    title: String,
    quiz: FirestoreQuiz,
    quizResults: List<QuizResult>,
    onQuizStateChanged: (QuizState) -> Unit,
    onDismiss: () -> Unit,
) {
    var isLoading by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = quiz) {
        isLoading = false
    }

    FullScreenDialog(
        isVisible = isVisible,
        title = title,
        onAction = null,
        onDismiss = onDismiss,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                if (isLoading)
                    LoadingBox()
                else
                    QuizStateChangeButton(
                        currentState = quiz.state,
                        onStateChange = { newState ->
                            isLoading = true
                            onQuizStateChanged(newState)
                        },
                    )
            }

            if (quiz.state == QuizState.FINISHED) {
                item {
                    WolczynText(
                        text = stringResource(Res.string.quiz_results),
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            textAlign = TextAlign.Center,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                items(items = quizResults, key = { it.userId }) { result ->
                    QuizResultCard(result = result)
                }
            } else {
                item {
                    WolczynText(
                        text = stringResource(Res.string.quiz_results_ongoing_message),
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            textAlign = TextAlign.Center,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
            }

            item {
                HeightSpacer(12.dp)
            }
        }
    }
}