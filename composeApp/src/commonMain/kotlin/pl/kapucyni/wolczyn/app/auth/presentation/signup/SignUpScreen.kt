package pl.kapucyni.wolczyn.app.auth.presentation.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpScreenState.PasswordErrorType
import pl.kapucyni.wolczyn.app.common.presentation.composables.DatePickDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDate
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.cd_clear_field
import smwolczyn.composeapp.generated.resources.create_account
import smwolczyn.composeapp.generated.resources.email
import smwolczyn.composeapp.generated.resources.email_error_invalid
import smwolczyn.composeapp.generated.resources.hide_password
import smwolczyn.composeapp.generated.resources.ok
import smwolczyn.composeapp.generated.resources.password
import smwolczyn.composeapp.generated.resources.password_error_too_short
import smwolczyn.composeapp.generated.resources.password_error_wrong
import smwolczyn.composeapp.generated.resources.show_password
import smwolczyn.composeapp.generated.resources.sign_in
import smwolczyn.composeapp.generated.resources.sign_up
import smwolczyn.composeapp.generated.resources.sign_up_successful_dialog_message
import smwolczyn.composeapp.generated.resources.sign_up_successful_dialog_title
import smwolczyn.composeapp.generated.resources.sign_up_user_exists_dialog_message
import smwolczyn.composeapp.generated.resources.sign_up_user_exists_dialog_title
import smwolczyn.composeapp.generated.resources.user_birthday

@Composable
fun SignUpScreen(
    navigateUp: () -> Unit,
    openSignIn: (String) -> Unit,
    viewModel: SignUpViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignUpScreenContent(
        navigateUp = navigateUp,
        openSignIn = openSignIn,
        state = state,
        updateEmail = viewModel::updateEmail,
        updatePassword = viewModel::updatePassword,
        updatePasswordHidden = viewModel::updatePasswordHidden,
        updateBirthdayDate = viewModel::updateBirthdayDate,
        signUp = viewModel::signUp,
        toggleSignUpSuccessVisibility = viewModel::toggleSignUpSuccessVisibility,
        toggleAccountExistsVisibility = viewModel::toggleAccountExistsVisibility,
        hideNoInternetDialog = viewModel::hideNoInternetDialog,
    )
}

@Composable
private fun SignUpScreenContent(
    navigateUp: () -> Unit,
    openSignIn: (String) -> Unit,
    state: SignUpScreenState,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    updatePasswordHidden: () -> Unit,
    updateBirthdayDate: (Long) -> Unit,
    signUp: () -> Unit,
    toggleSignUpSuccessVisibility: () -> Unit,
    toggleAccountExistsVisibility: () -> Unit,
    hideNoInternetDialog: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val (emailRef, passwordRef) = remember { FocusRequester.createRefs() }
    var dateDialogVisible by rememberSaveable { mutableStateOf(false) }

    ScreenLayout(
        title = stringResource(Res.string.sign_up),
        onBackPressed = navigateUp,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
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
                isError = state.emailError,
                supportingText = if (state.emailError) {
                    {
                        WolczynText(text = stringResource(Res.string.email_error_invalid))
                    }
                } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailRef)
                    .focusProperties { next = passwordRef }
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = updatePassword,
                singleLine = true,
                label = { WolczynText(text = stringResource(Res.string.password)) },
                visualTransformation = if (state.passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
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
                                    PasswordErrorType.TOO_SHORT -> Res.string.password_error_too_short
                                    PasswordErrorType.WRONG -> Res.string.password_error_wrong
                                }
                            )
                        )
                    }
                } else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordRef)
            )

            OutlinedTextField(
                value = state.birthdayDate?.getFormattedDate().orEmpty(),
                onValueChange = {},
                label = { WolczynText(stringResource(Res.string.user_birthday)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                },
                enabled = false,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .clickable { dateDialogVisible = true }
                    .fillMaxWidth(),
            )

            Button(
                onClick = {
                    focusManager.clearFocus(true)
                    signUp()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                WolczynText(text = stringResource(Res.string.create_account))
            }
        }
    }

    DatePickDialog(
        isVisible = dateDialogVisible,
        dateMillis = state.birthdayDate,
        onDismiss = { dateDialogVisible = false },
        onDateSelected = updateBirthdayDate,
    )

    LoadingDialog(visible = state.loading)

    WolczynAlertDialog(
        isVisible = state.isSignedUpDialogVisible,
        imageVector = Icons.Outlined.ManageAccounts,
        dialogTitleId = Res.string.sign_up_successful_dialog_title,
        dialogTextId = Res.string.sign_up_successful_dialog_message,
        confirmBtnTextId = Res.string.ok,
        onConfirm = {
            toggleSignUpSuccessVisibility()
            openSignIn(state.email)
        },
        dismissible = false,
    )

    WolczynAlertDialog(
        isVisible = state.accountExistsDialogVisible,
        imageVector = Icons.Outlined.PersonSearch,
        dialogTitleId = Res.string.sign_up_user_exists_dialog_title,
        dialogTextId = Res.string.sign_up_user_exists_dialog_message,
        confirmBtnTextId = Res.string.sign_in,
        onConfirm = {
            toggleAccountExistsVisibility()
            openSignIn(state.email)
        },
        dismissBtnTextId = Res.string.cancel,
        onDismissRequest = {
            toggleAccountExistsVisibility()
        },
        dismissible = false,
    )

    NoInternetDialog(
        isVisible = state.noInternetDialogVisible,
        onReconnect = {
            hideNoInternetDialog()
            signUp()
        },
        onDismiss = hideNoInternetDialog,
    )
}