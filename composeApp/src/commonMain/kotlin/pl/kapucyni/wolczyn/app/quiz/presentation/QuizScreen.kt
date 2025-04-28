package pl.kapucyni.wolczyn.app.quiz.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.qualifier.named
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizState
import pl.kapucyni.wolczyn.app.quiz.presentation.composables.QuizQuestionsList
import pl.kapucyni.wolczyn.app.quiz.presentation.composables.QuizTitle
import pl.kapucyni.wolczyn.app.quiz.presentation.model.QuizScreenAction
import pl.kapucyni.wolczyn.app.quiz.presentation.model.QuizScreenState
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.quiz
import smwolczyn.composeapp.generated.resources.quiz_loading_error
import smwolczyn.composeapp.generated.resources.quiz_result
import smwolczyn.composeapp.generated.resources.quiz_user_not_allowed
import smwolczyn.composeapp.generated.resources.start_quiz
import smwolczyn.composeapp.generated.resources.user_id
import smwolczyn.composeapp.generated.resources.user_id_title

@OptIn(KoinExperimentalAPI::class)
@Composable
fun QuizScreen(
    quizType: String,
    onBackPressed: () -> Unit,
    viewModel: QuizViewModel = koinViewModel(qualifier = named(quizType)),
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    ScreenLayout(
        title = stringResource(Res.string.quiz),
        onBackPressed = onBackPressed
    ) {
        QuizScreenContent(
            state = screenState,
            handleScreenAction = viewModel::handleScreenAction,
        )
    }
}

@Composable
fun QuizScreenContent(
    state: QuizScreenState,
    handleScreenAction: (QuizScreenAction) -> Unit,
) {
    if (state.isLoading)
        LoadingBox()
    else if (state.loadingFailed)
        QuizTitle(text = stringResource(Res.string.quiz_loading_error))

    AnimatedVisibility(
        visible =
        state.quiz?.state == QuizState.ONGOING && !state.quizStarted && !state.quizFinished,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            QuizTitle(text = stringResource(Res.string.user_id_title))

            OutlinedTextField(
                value = state.userId,
                onValueChange = { handleScreenAction(QuizScreenAction.UpdateUserId(it)) },
                singleLine = true,
                placeholder = { WolczynText(text = stringResource(Res.string.user_id)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                isError = state.userNotAllowed,
                supportingText = if (state.userNotAllowed) {
                    {
                        WolczynText(text = stringResource(Res.string.quiz_user_not_allowed))
                    }
                } else null,
                modifier = Modifier.padding(16.dp),
            )

            Button(
                onClick = { handleScreenAction(QuizScreenAction.StartQuizClicked) },
                enabled = state.userId.isNotBlank() && !state.quizStarted,
                modifier = Modifier.fillMaxWidth(0.5f),
            ) {
                WolczynText(stringResource(Res.string.start_quiz))
            }
        }
    }

    AnimatedVisibility(
        visible = state.quiz?.state == QuizState.ONGOING && state.quizStarted && !state.quizFinished,
    ) {
        state.quiz?.let {
            QuizQuestionsList(
                questions = state.quiz.questions,
                onAnswerSelected = { question, answer ->
                    handleScreenAction(QuizScreenAction.AnswerSelected(question, answer))
                },
                onFinishQuiz = { handleScreenAction(QuizScreenAction.FinishQuizClicked) },
                isFinished = false,
            )
        }
    }

    AnimatedVisibility(
        visible = state.quiz?.state == QuizState.ONGOING && state.quizFinished && !state.quizStarted,
    ) {
        QuizTitle(text = stringResource(Res.string.quiz_result, state.score))
    }

    AnimatedVisibility(
        visible = state.quiz?.state == QuizState.FINISHED,
    ) {
        state.quiz?.let {
            QuizQuestionsList(
                questions = state.quiz.questions,
                isFinished = true,
            )
        }
    }
}