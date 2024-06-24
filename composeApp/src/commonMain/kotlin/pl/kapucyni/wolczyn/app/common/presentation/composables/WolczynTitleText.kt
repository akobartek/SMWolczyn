package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import pl.kapucyni.wolczyn.app.theme.appColorPrimary
import pl.kapucyni.wolczyn.app.theme.poppinsFontFamily

@Composable
fun WolczynTitleText(
    text: String,
    color: Color = appColorPrimary,
    textAlign: TextAlign = TextAlign.Unspecified,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.uppercase(),
        fontFamily = poppinsFontFamily(),
        style = TextStyle(
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            textAlign = textAlign
        ),
        modifier = modifier
    )
}