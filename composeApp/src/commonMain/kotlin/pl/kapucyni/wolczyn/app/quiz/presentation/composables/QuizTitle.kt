package pl.kapucyni.wolczyn.app.quiz.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText

@Composable
fun QuizTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    WolczynText(
        text = text,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            textAlign = TextAlign.Center,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}