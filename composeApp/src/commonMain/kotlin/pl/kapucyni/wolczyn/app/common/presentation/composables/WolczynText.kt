package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import pl.kapucyni.wolczyn.app.theme.poppinsFontFamily

@Composable
fun WolczynText(
    text: String,
    textStyle: TextStyle? = null,
    isOneLiner: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontFamily = poppinsFontFamily(),
        maxLines = if (isOneLiner) 1 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis,
        style = textStyle ?: LocalTextStyle.current,
        modifier = modifier
    )
}

@Composable
fun WolczynText(
    text: AnnotatedString,
    textStyle: TextStyle? = null,
    isOneLiner: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontFamily = poppinsFontFamily(),
        maxLines = if (isOneLiner) 1 else Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis,
        style = textStyle ?: LocalTextStyle.current,
        modifier = modifier
    )
}