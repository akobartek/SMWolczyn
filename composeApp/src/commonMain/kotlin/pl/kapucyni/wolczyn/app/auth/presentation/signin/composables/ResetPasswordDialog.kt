package pl.kapucyni.wolczyn.app.auth.presentation.signin.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.cd_clear_field
import smwolczyn.composeapp.generated.resources.email
import smwolczyn.composeapp.generated.resources.reset_password_dialog_message
import smwolczyn.composeapp.generated.resources.reset_password_dialog_title
import smwolczyn.composeapp.generated.resources.reset_password_error
import smwolczyn.composeapp.generated.resources.send

@Composable
fun ResetPasswordDialog(
    signInEmail: String,
    onReset: (String) -> Unit,
    onCancel: () -> Unit,
    isError: Boolean,
) {
    var email by rememberSaveable { mutableStateOf(signInEmail) }

    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        title = {
            WolczynText(text = stringResource(Res.string.reset_password_dialog_title))
        },
        text = {
            Column {
                WolczynText(text = stringResource(Res.string.reset_password_dialog_message))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
                    placeholder = { WolczynText(text = stringResource(Res.string.email)) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    trailingIcon = {
                        if (email.isNotBlank())
                            IconButton(onClick = { email = "" }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = stringResource(Res.string.cd_clear_field),
                                )
                            }
                    },
                    isError = isError,
                    supportingText = if (isError) {
                        {
                            WolczynText(text = stringResource(Res.string.reset_password_error))
                        }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                )
            }
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = { onReset(email) }) {
                WolczynText(stringResource(Res.string.send))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                WolczynText(stringResource(Res.string.cancel))
            }
        },
    )
}