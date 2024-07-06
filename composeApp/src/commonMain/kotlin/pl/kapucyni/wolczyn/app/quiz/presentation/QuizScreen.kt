package pl.kapucyni.wolczyn.app.quiz.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.utils.collectAsStateMultiplatform
import pl.kapucyni.wolczyn.app.quiz.presentation.model.QuizScreenState
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.quiz

@Composable
fun QuizScreen(
    quizType: String,
    onBackPressed: () -> Unit,
    viewModel: QuizViewModel = koinInject(qualifier = named(quizType)),
) {
    val screenState by viewModel.screenState.collectAsStateMultiplatform()

    ScreenLayout(
        title = stringResource(Res.string.quiz),
        onBackPressed = onBackPressed
    ) {
        QuizScreenContent(screenState)
    }
}

@Composable
fun QuizScreenContent(
    state: QuizScreenState,
) {
    // TODO()
    println(state.quiz)
}