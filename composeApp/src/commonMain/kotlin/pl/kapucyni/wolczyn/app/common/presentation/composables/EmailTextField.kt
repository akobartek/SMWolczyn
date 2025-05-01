package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.email

@Composable
fun EmailTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { WolczynText(text = stringResource(Res.string.email)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
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
        modifier = Modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth()
            .then(modifier),
    )
}