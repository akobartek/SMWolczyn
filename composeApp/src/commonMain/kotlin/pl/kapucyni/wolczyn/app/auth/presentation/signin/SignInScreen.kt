package pl.kapucyni.wolczyn.app.auth.presentation.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.auth.presentation.signin.SignInScreenState.EmailErrorType
import pl.kapucyni.wolczyn.app.auth.presentation.signin.SignInScreenState.NoInternetAction
import pl.kapucyni.wolczyn.app.auth.presentation.signin.SignInScreenState.PasswordErrorType
import pl.kapucyni.wolczyn.app.auth.presentation.signin.composables.ResetPasswordDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NavigateUpIcon
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynLogo
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.cd_clear_field
import smwolczyn.composeapp.generated.resources.email
import smwolczyn.composeapp.generated.resources.email_error_invalid
import smwolczyn.composeapp.generated.resources.email_error_no_user
import smwolczyn.composeapp.generated.resources.forgot_password
import smwolczyn.composeapp.generated.resources.hide_password
import smwolczyn.composeapp.generated.resources.password
import smwolczyn.composeapp.generated.resources.password_error_empty
import smwolczyn.composeapp.generated.resources.password_error_invalid
import smwolczyn.composeapp.generated.resources.password_error_unknown
import smwolczyn.composeapp.generated.resources.show_password
import smwolczyn.composeapp.generated.resources.sign_in
import smwolczyn.composeapp.generated.resources.sign_up
import smwolczyn.composeapp.generated.resources.verify_email_dialog_message
import smwolczyn.composeapp.generated.resources.verify_email_dialog_title
import smwolczyn.composeapp.generated.resources.verify_email_send_again

@Composable
fun SignInScreen(
    navigateUp: () -> Unit,
    openSignUp: (String) -> Unit,
    viewModel: SignInViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignInScreenContent(
        navigateUp = navigateUp,
        state = state,
        updateEmail = viewModel::updateEmail,
        updatePassword = viewModel::updatePassword,
        updatePasswordHidden = viewModel::updatePasswordHidden,
        signIn = viewModel::signIn,
        signUp = openSignUp,
        sendResetPasswordEmail = viewModel::sendResetPasswordEmail,
        toggleForgottenPasswordDialogVisibility = viewModel::toggleForgottenPasswordDialogVisibility,
        hideNoInternetDialog = viewModel::hideNoInternetDialog,
        toggleEmailUnverifiedDialogVisibility = viewModel::toggleEmailUnverifiedDialogVisibility,
    )
}

@Composable
private fun SignInScreenContent(
    navigateUp: () -> Unit,
    state: SignInScreenState,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    updatePasswordHidden: () -> Unit,
    signIn: () -> Unit,
    signUp: (String) -> Unit,
    sendResetPasswordEmail: (String) -> Unit,
    toggleForgottenPasswordDialogVisibility: () -> Unit,
    hideNoInternetDialog: () -> Unit,
    toggleEmailUnverifiedDialogVisibility: (Boolean) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val (emailRef, passwordRef) = remember { FocusRequester.createRefs() }

    LaunchedEffect(state) {
        if (state.isSignedIn)
            navigateUp()
    }

    BoxWithConstraints {
        val isCompactHeight = maxHeight < 480.dp

        ScreenLayout(
            title = null,
            onBackPressed = navigateUp,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp),
            ) {
                WolczynLogo(modifier = Modifier.padding(top = if (isCompactHeight) 16.dp else 60.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = updateEmail,
                    singleLine = true,
                    label = { WolczynText(text = stringResource(Res.string.email)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) },
                    ),
                    trailingIcon = {
                        if (state.email.isNotBlank())
                            IconButton(onClick = { updateEmail("") }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = stringResource(Res.string.cd_clear_field)
                                )
                            }
                    },
                    isError = state.emailError != null,
                    supportingText = if (state.emailError != null) {
                        {
                            WolczynText(
                                text = stringResource(
                                    when (state.emailError) {
                                        EmailErrorType.INVALID -> Res.string.email_error_invalid
                                        EmailErrorType.NO_USER -> Res.string.email_error_no_user
                                    }
                                )
                            )
                        }
                    } else null,
                    modifier = Modifier
                        .widthIn(max = 420.dp)
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .focusRequester(emailRef)
                        .focusProperties { next = passwordRef }
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = updatePassword,
                    singleLine = true,
                    label = { WolczynText(text = stringResource(Res.string.password)) },
                    visualTransformation =
                        if (state.passwordHidden) PasswordVisualTransformation()
                        else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            signIn()
                        },
                    ),
                    trailingIcon = {
                        IconButton(onClick = updatePasswordHidden) {
                            if (state.passwordHidden)
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
                    isError = state.passwordError != null,
                    supportingText = if (state.passwordError != null) {
                        {
                            WolczynText(
                                text = stringResource(
                                    when (state.passwordError) {
                                        PasswordErrorType.EMPTY -> Res.string.password_error_empty
                                        PasswordErrorType.INVALID -> Res.string.password_error_invalid
                                        PasswordErrorType.UNKNOWN -> Res.string.password_error_unknown
                                    }
                                )
                            )
                        }
                    } else null,
                    modifier = Modifier
                        .widthIn(max = 420.dp)
                        .fillMaxWidth()
                        .focusRequester(passwordRef)
                )

                Button(
                    onClick = {
                        focusManager.clearFocus(true)
                        signIn()
                    },
                    modifier = Modifier
                        .widthIn(max = 420.dp)
                        .fillMaxWidth(),
                ) {
                    WolczynText(text = stringResource(Res.string.sign_in))
                }

                HeightSpacer(4.dp)

                OutlinedButton(
                    onClick = toggleForgottenPasswordDialogVisibility,
                    modifier = Modifier
                        .widthIn(max = 420.dp)
                        .fillMaxWidth(0.75f),
                ) {
                    WolczynText(text = stringResource(Res.string.forgot_password))
                }

                OutlinedButton(
                    onClick = {
                        focusManager.clearFocus(true)
                        signUp(state.email)
                    },
                    modifier = Modifier
                        .widthIn(max = 420.dp)
                        .fillMaxWidth(0.75f),
                ) {
                    WolczynText(text = stringResource(Res.string.sign_up))
                }

                HeightSpacer(8.dp)
            }

            LoadingDialog(visible = state.loading)

            if (state.forgottenPasswordDialogVisible)
                ResetPasswordDialog(
                    signInEmail = state.email,
                    onReset = sendResetPasswordEmail,
                    onCancel = toggleForgottenPasswordDialogVisibility,
                    isError = state.forgottenPasswordDialogError,
                )

            WolczynAlertDialog(
                isVisible = state.emailUnverifiedDialogVisible,
                imageVector = Icons.Outlined.ErrorOutline,
                dialogTitleId = Res.string.verify_email_dialog_title,
                dialogTextId = Res.string.verify_email_dialog_message,
                confirmBtnTextId = Res.string.verify_email_send_again,
                onConfirm = { toggleEmailUnverifiedDialogVisibility(true) },
                dismissible = false,
                dismissBtnTextId = Res.string.cancel,
                onDismissRequest = { toggleEmailUnverifiedDialogVisibility(false) },
            )

            NoInternetDialog(
                isVisible = state.noInternetAction != null,
                onReconnect = {
                    hideNoInternetDialog()
                    when (state.noInternetAction) {
                        NoInternetAction.SIGN_IN -> signIn()
                        NoInternetAction.RESET_PASSWORD -> toggleForgottenPasswordDialogVisibility()
                        else -> {}
                    }
                },
                onDismiss = hideNoInternetDialog,
            )
        }

        NavigateUpIcon(
            navigateUp = navigateUp,
            modifier = Modifier.padding(vertical = 16.dp),
        )
    }
}