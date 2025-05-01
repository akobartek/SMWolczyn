package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.hide_password
import smwolczyn.composeapp.generated.resources.password
import smwolczyn.composeapp.generated.resources.show_password

@Composable
fun PasswordTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    enabled: Boolean = true,
    keyboardActions: KeyboardActions? = null,
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { WolczynText(text = stringResource(Res.string.password)) },
        visualTransformation =
            if (passwordHidden) PasswordVisualTransformation()
            else VisualTransformation.None,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = keyboardActions?.let { ImeAction.Next } ?: ImeAction.Done,
        ),
        keyboardActions = keyboardActions ?: KeyboardActions.Default,
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                if (passwordHidden)
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = stringResource(Res.string.show_password)
                    )
                else
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(Res.string.hide_password)
                    )
            }
        },
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