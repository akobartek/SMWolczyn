package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_clear_field
import smwolczyn.composeapp.generated.resources.forgot_password
import smwolczyn.composeapp.generated.resources.login
import smwolczyn.composeapp.generated.resources.login_error
import smwolczyn.composeapp.generated.resources.password
import smwolczyn.composeapp.generated.resources.sign_in
import smwolczyn.composeapp.generated.resources.sign_up

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthSignIn(
    isVisible: Boolean,
    signInError: Boolean,
    onSignIn: (String, String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val (loginRef, passwordRef) = remember { FocusRequester.createRefs() }
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    if (isVisible)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp)
        ) {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                singleLine = true,
                label = { Text(text = stringResource(Res.string.login)) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }),
                trailingIcon = {
                    if (login.isNotBlank())
                        IconButton(onClick = { login = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(Res.string.cd_clear_field)
                            )
                        }
                },
                isError = signInError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .focusRequester(loginRef)
                    .focusProperties { next = passwordRef }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                label = { Text(text = stringResource(Res.string.password)) },
                visualTransformation =
                if (passwordHidden) PasswordVisualTransformation()
                else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                trailingIcon = {
                    IconButton(onClick = { passwordHidden = !passwordHidden }) {
                        if (passwordHidden)
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = null
                            )
                        else
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = null
                            )
                    }
                },
                isError = signInError,
                supportingText = if (signInError) {
                    { Text(text = stringResource(Res.string.login_error)) }
                } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .focusRequester(passwordRef)
            )
            HeightSpacer(8.dp)
            Button(
                onClick = {
                    focusManager.clearFocus(true)
                    onSignIn(login, password)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(Res.string.sign_in))
            }
            HeightSpacer(8.dp)
            // TODO -> GUGIEL
//        OutlinedButton(
//            onClick = {
//                focusManager.clearFocus(true)
//                screenModel.signUp()
//            },
//            modifier = Modifier.fillMaxWidth(0.75f)
//        ) {
//            Text(text = stringResource(id = R.string.sign_up))
//        }
//        HeightSpacer(8.dp)
            OutlinedButton(
                onClick = { uriHandler.openUri("https://konto.kapucyni.pl/remind") },
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text(text = stringResource(Res.string.forgot_password))
            }
            HeightSpacer(8.dp)
            OutlinedButton(
                onClick = { uriHandler.openUri("https://konto.kapucyni.pl/register") },
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text(text = stringResource(Res.string.sign_up))
            }
        }
}