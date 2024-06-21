package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import pl.kapucyni.wolczyn.app.theme.poppinsFontFamily

@Composable
fun WolczynText(
    text: String,
    textStyle: TextStyle = TextStyle(),
    isOneLiner: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontFamily = poppinsFontFamily(),
        maxLines = if (isOneLiner) 1 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis,
        style = textStyle,
        modifier = modifier
    )
}