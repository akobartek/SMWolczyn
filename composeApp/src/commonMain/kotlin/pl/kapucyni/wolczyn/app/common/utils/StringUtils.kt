package pl.kapucyni.wolczyn.app.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpPasswordError
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

fun CharSequence.validatePassword(): SignUpPasswordError? {
    val passwordRegex = Regex("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,20})")
    return when {
        this.length < 8 -> SignUpPasswordError.TOO_SHORT
        this.matches(passwordRegex).not() -> SignUpPasswordError.WRONG
        else -> null
    }
}

fun CharSequence.isValidPhoneNumber(): Boolean {
    val regex = Regex("^[1-9]\\d{8}$")
    return this.matches(regex)
}

fun CharSequence.isValidPesel(): Boolean {
    val regex = Regex("\\b[0-9]{2}([02468][1-9]|[13579][0-2])(0[1-9]|[1,2][0-9]|3[0-1])\\d{5}")
    return this.matches(regex) && peselControlNumberValidation()
}

private fun CharSequence.peselControlNumberValidation(): Boolean = try {
    map { it.digitToInt() }.let { digits ->
        val wages = listOf(1, 3, 7, 9, 1, 3, 7, 9, 1, 3)
        val controlNumber = digits.last()
        val calculatedNumber = digits.take(10)
            .zip(wages)
            .sumOf { (digit, wage) -> digit * wage % 10 }
            .let { sum -> (10 - sum % 10) % 10 }
        controlNumber == calculatedNumber
    }
} catch (_: Exception) {
    false
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