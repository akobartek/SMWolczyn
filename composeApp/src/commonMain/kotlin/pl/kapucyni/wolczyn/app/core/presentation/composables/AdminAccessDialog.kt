package pl.kapucyni.wolczyn.app.core.presentation.composables

import SMWolczyn.composeApp.BuildConfig
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynLogo
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.admin_password_error
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.cd_clear_field
import smwolczyn.composeapp.generated.resources.password
import smwolczyn.composeapp.generated.resources.restricted_access
import smwolczyn.composeapp.generated.resources.sign_in

@Composable
fun AdminAccessDialog(
    isVisible: Boolean,
    onAccess: () -> Unit,
    onCancel: () -> Unit,
) {
    if (isVisible) {
        var password by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(password) {
            if (isError) isError = false
        }

        AlertDialog(
            icon = {
                WolczynLogo(size = 36.dp)
            },
            title = {
                WolczynText(
                    text = stringResource(Res.string.restricted_access),
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Center
                    ),
                )
            },
            text = {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    placeholder = { WolczynText(text = stringResource(Res.string.password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        if (password.isNotBlank())
                            IconButton(onClick = { password = "" }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = stringResource(Res.string.cd_clear_field)
                                )
                            }
                    },
                    isError = isError,
                    supportingText = if (isError) {
                        {
                            WolczynText(text = stringResource(Res.string.admin_password_error))
                        }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            },
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    if (password == BuildConfig.ADMIN_PASSWORD) onAccess()
                    else isError = true
                }) {
                    WolczynText(stringResource(Res.string.sign_in))
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    WolczynText(stringResource(Res.string.cancel))
                }
            },
        )
    }
}