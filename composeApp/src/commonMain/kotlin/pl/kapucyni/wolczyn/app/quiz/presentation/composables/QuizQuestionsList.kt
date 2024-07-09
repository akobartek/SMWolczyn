package pl.kapucyni.wolczyn.app.quiz.presentation.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizAnswer
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizQuestion
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.button_finish
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.finish_quiz
import smwolczyn.composeapp.generated.resources.quiz_finish_message
import smwolczyn.composeapp.generated.resources.quiz_finish_title

@Composable
fun QuizQuestionsList(
    questions: List<QuizQuestion>,
    onAnswerSelected: (QuizQuestion, QuizAnswer) -> Unit = { _, _ -> },
    onFinishQuiz: () -> Unit = {},
    isFinished: Boolean,
) {
    var isFinishQuizDialogVisible by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
    ) {
        questions.forEach { question ->
            item {
                QuizTitle(text = question.question,)
            }
            items(items = question.answers, key = { it.answer }) { answer ->
                AnswerCard(
                    answer = answer,
                    onAnswerSelected = { onAnswerSelected(question, answer) },
                    isFinished = isFinished,
                )
            }
            item { HeightSpacer(24.dp) }
        }

        if (!isFinished)
            item {
                Button(
                    onClick = { isFinishQuizDialogVisible = true },
                    enabled = questions.all { question -> question.answers.any { it.isSelected } },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(vertical = 12.dp),
                ) {
                    WolczynText(stringResource(Res.string.finish_quiz))
                }
            }
    }

    WolczynAlertDialog(
        isVisible = isFinishQuizDialogVisible,
        imageVector = Icons.Default.Checklist,
        dialogTitleId = Res.string.quiz_finish_title,
        dialogTextId = Res.string.quiz_finish_message,
        confirmBtnTextId = Res.string.button_finish,
        onConfirm = {
            isFinishQuizDialogVisible = false
            onFinishQuiz()
        },
        dismissible = false,
        dismissBtnTextId = Res.string.cancel,
        onDismissRequest = { isFinishQuizDialogVisible = false }
    )
}