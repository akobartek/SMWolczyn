package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.first_name_error
import smwolczyn.composeapp.generated.resources.user_first_name

@Composable
fun FirstNameTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    error: Boolean,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { WolczynText(text = stringResource(Res.string.user_first_name)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = keyboardActions?.let { ImeAction.Next } ?: ImeAction.Done,
            capitalization = KeyboardCapitalization.Words,
        ),
        keyboardActions = keyboardActions ?: KeyboardActions.Default,
        enabled = enabled,
        singleLine = true,
        isError = error,
        supportingText = if (error) {
            {
                WolczynText(text = stringResource(Res.string.first_name_error))
            }
        } else null,
        modifier = Modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth()
            .then(modifier),
    )
}