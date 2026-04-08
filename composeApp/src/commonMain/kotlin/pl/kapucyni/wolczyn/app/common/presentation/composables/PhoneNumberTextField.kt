package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.theme.AppTheme
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.phone_number

@Composable
fun PhoneNumberTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val cleanedInput = input
                .replace(Regex("^(\\+48|0048)"), "")
                .filter { it.isDigit() }
                .take(9)
            onValueChange(cleanedInput)
        },
        label = { WolczynText(text = stringResource(Res.string.phone_number)) },
        prefix = { WolczynText(text = "+48 ") },
        visualTransformation = PhoneVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Phone,
            imeAction = keyboardActions?.let { ImeAction.Next } ?: ImeAction.Done,
        ),
        keyboardActions = keyboardActions ?: KeyboardActions.Default,
        trailingIcon = trailingIcon,
        enabled = enabled,
        singleLine = true,
        isError = errorMessage != null,
        supportingText = errorMessage?.let {
            {
                WolczynText(text = errorMessage)
            }
        },
        modifier = modifier.fillMaxWidth(),
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = AndroidUiModes.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PhoneNumberTextFieldPreview() {
    var phoneNumber by remember { mutableStateOf("") }
    AppTheme {
        PhoneNumberTextField(
            value = phoneNumber,
            onValueChange = {},
            errorMessage = null,
        )
    }
}

private class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 9) text.text.substring(0..8) else text.text

        val out = StringBuilder()
        for (i in trimmed.indices) {
            out.append(trimmed[i])
            if ((i == 2 || i == 5) && i != trimmed.lastIndex) {
                out.append(" ")
            }
        }

        val phoneNumberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 6) return offset + 1
                return offset + 2
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset - 1
                return offset - 2
            }
        }

        return TransformedText(AnnotatedString(out.toString()), phoneNumberOffsetTranslator)
    }
}