package pl.kapucyni.wolczyn.app.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.theme.wolczynColors

expect fun String.normalizeMultiplatform(): String

expect fun randomUUID(): String

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    )
    return this.matches(emailRegex)
}

@Composable
fun buildLinkableString(
    text: StringResource,
    links: List<Triple<String, String, StringResource>>,
) = buildAnnotatedString {
    stringResource(text)
        .split(*(links.map { it.first }.toTypedArray()))
        .let { split ->
            append(split[0])
            links.forEachIndexed { index, (_, url, textRes) ->
                withLink(
                    LinkAnnotation.Url(
                        url = url,
                        styles = TextLinkStyles(style = SpanStyle(color = wolczynColors.primary)),
                    )
                ) {
                    append(stringResource(textRes))
                }
                append(split[index + 1])
            }
        }
}