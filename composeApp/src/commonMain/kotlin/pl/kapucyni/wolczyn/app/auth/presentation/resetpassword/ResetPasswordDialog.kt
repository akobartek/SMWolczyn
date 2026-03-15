package pl.kapucyni.wolczyn.app.auth.presentation.resetpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.CloseResetDialog
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.SetNewPassword
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpPasswordError.TOO_SHORT
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpPasswordError.WRONG
import pl.kapucyni.wolczyn.app.common.presentation.composables.PasswordTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.theme.AppTheme
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.ic_lock
import smwolczyn.composeapp.generated.resources.password_error_too_short
import smwolczyn.composeapp.generated.resources.password_new
import smwolczyn.composeapp.generated.resources.reset_password_dialog_title
import smwolczyn.composeapp.generated.resources.reset_password_error
import smwolczyn.composeapp.generated.resources.reset_password_set_dialog_message
import smwolczyn.composeapp.generated.resources.save

@Composable
fun ResetPasswordDialog(
    state: ResetPasswordDialogState?,
    handleAction: (AuthAction) -> Unit,
) {
    state?.let {
        var password by rememberSaveable { mutableStateOf("") }

        AlertDialog(
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
            title = {
                WolczynText(text = stringResource(Res.string.reset_password_dialog_title))
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (state.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                    } else {
                        WolczynText(
                            text = buildAnnotatedString {
                                append(stringResource(Res.string.reset_password_set_dialog_message))
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(state.email)
                                }
                            },
                            textStyle = TextStyle(textAlign = TextAlign.Center),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        PasswordTextField(
                            label = Res.string.password_new,
                            value = password,
                            onValueChange = { password = it },
                            errorMessage = when {
                                state.passwordError == TOO_SHORT -> Res.string.password_error_too_short
                                state.passwordError == WRONG -> Res.string.password_error_too_short
                                state.resetFailed -> Res.string.reset_password_error
                                else -> null
                            }?.let { stringResource(it) },
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            },
            onDismissRequest = {},
            confirmButton = {
                if (state.loading.not())
                    TextButton(onClick = { handleAction(SetNewPassword(password)) }) {
                        WolczynText(stringResource(Res.string.save))
                    }
            },
            dismissButton = {
                if (state.loading.not())
                    TextButton(onClick = { handleAction(CloseResetDialog) }) {
                        WolczynText(stringResource(Res.string.cancel))
                    }
            },
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ResetPasswordDialog(
            state = ResetPasswordDialogState(
                resetCode = "123456",
                email = "sokolowskijbartek@gmail.com",
            ),
            handleAction = {},
        )
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    AppTheme {
        ResetPasswordDialog(
            state = ResetPasswordDialogState(
                resetCode = "123456",
                email = "sokolowskijbartek@gmail.com",
                loading = true
            ),
            handleAction = {},
        )
    }
}