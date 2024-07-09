package pl.kapucyni.wolczyn.app.quiz.presentation.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.quiz.domain.model.QuizAnswer

@Composable
fun AnswerCard(
    answer: QuizAnswer,
    onAnswerSelected: () -> Unit,
    isFinished: Boolean,
) {
    val colors =
        if (isFinished && answer.isCorrect)
            CardDefaults.cardColors(containerColor = Color(0xFF4BB543))
        else if (!isFinished && answer.isSelected)
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        else CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)

    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground),
        onClick = {
            if (!answer.isSelected) onAnswerSelected()
        },
        colors = colors,
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
    ) {
        WolczynText(
            text = answer.answer,
            textStyle = MaterialTheme.typography.titleMedium.copy(
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}