package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.pesel
import smwolczyn.composeapp.generated.resources.pesel_error

@Composable
fun PeselTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    error: Boolean,
    visible: Boolean = true,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    var peselValue by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(value) {
        peselValue = TextFieldValue(value, TextRange(value.length))
    }

    AnimatedVisibility(visible) {
        OutlinedTextField(
            value = peselValue,
            onValueChange = { newValue ->
                when {
                    newValue.text != value -> onValueChange(newValue.text)
                    (newValue.selection.start >= 6) -> peselValue = newValue
                }
            },
            label = { WolczynText(text = stringResource(Res.string.pesel)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = keyboardActions?.let { ImeAction.Next } ?: ImeAction.Done,
            ),
            keyboardActions = keyboardActions ?: KeyboardActions.Default,
            trailingIcon = trailingIcon,
            enabled = enabled,
            singleLine = true,
            isError = error,
            supportingText = if (error) {
                {
                    WolczynText(text = stringResource(Res.string.pesel_error))
                }
            } else null,
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .then(modifier),
        )
    }
}