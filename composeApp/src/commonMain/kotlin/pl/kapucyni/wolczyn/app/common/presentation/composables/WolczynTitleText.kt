package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import pl.kapucyni.wolczyn.app.theme.poppinsFontFamily
import pl.kapucyni.wolczyn.app.theme.wolczynColors

@Composable
fun WolczynTitleText(
    text: String,
    color: Color? = wolczynColors.primary,
    textAlign: TextAlign = TextAlign.Unspecified,
    modifier: Modifier = Modifier
) {
    val style = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        textAlign = textAlign
    )

    Text(
        text = text.uppercase(),
        fontFamily = poppinsFontFamily(),
        style = color?.let { style.copy(color = it) } ?: style,
        modifier = modifier
    )
}