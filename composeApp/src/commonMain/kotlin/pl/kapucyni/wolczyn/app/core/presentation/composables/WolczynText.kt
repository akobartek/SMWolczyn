package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import pl.kapucyni.wolczyn.app.theme.poppinsFontFamily

@Composable
fun WolczynText(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.uppercase(),
        fontFamily = poppinsFontFamily(),
        style = textStyle,
        modifier = modifier
    )
}