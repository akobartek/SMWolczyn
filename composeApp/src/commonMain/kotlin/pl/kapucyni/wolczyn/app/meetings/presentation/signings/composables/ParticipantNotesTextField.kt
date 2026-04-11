package pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText

@Composable
fun ParticipantNotesTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: StringResource,
    placeholder: StringResource,
    errorMessage: StringResource?,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    AnimatedVisibility(enabled) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { WolczynText(text = stringResource(label)) },
            placeholder = { WolczynText(text = stringResource(placeholder)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = keyboardActions?.let { ImeAction.Next } ?: ImeAction.Done,
            ),
            keyboardActions = keyboardActions ?: KeyboardActions.Default,
            trailingIcon = trailingIcon,
            enabled = enabled,
            minLines = 3,
            maxLines = 5,
            isError = errorMessage != null,
            supportingText = errorMessage?.let {
                {
                    WolczynText(text = stringResource(errorMessage))
                }
            },
            modifier = modifier.fillMaxWidth(),
        )
    }
}