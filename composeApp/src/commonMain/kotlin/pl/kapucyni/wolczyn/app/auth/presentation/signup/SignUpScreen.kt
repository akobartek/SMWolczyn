package pl.kapucyni.wolczyn.app.auth.presentation.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.*
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpScreenState.PasswordErrorType
import pl.kapucyni.wolczyn.app.common.presentation.composables.DatePickDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDate
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.birthday_error
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.city_error
import smwolczyn.composeapp.generated.resources.create_account
import smwolczyn.composeapp.generated.resources.email
import smwolczyn.composeapp.generated.resources.email_error_invalid
import smwolczyn.composeapp.generated.resources.first_name_error
import smwolczyn.composeapp.generated.resources.hide_password
import smwolczyn.composeapp.generated.resources.last_name_error
import smwolczyn.composeapp.generated.resources.ok
import smwolczyn.composeapp.generated.resources.password
import smwolczyn.composeapp.generated.resources.password_error_too_short
import smwolczyn.composeapp.generated.resources.password_error_wrong
import smwolczyn.composeapp.generated.resources.show_password
import smwolczyn.composeapp.generated.resources.sign_in
import smwolczyn.composeapp.generated.resources.sign_up
import smwolczyn.composeapp.generated.resources.sign_up_consents
import smwolczyn.composeapp.generated.resources.sign_up_data_processing
import smwolczyn.composeapp.generated.resources.sign_up_privacy_policy
import smwolczyn.composeapp.generated.resources.sign_up_successful_dialog_message
import smwolczyn.composeapp.generated.resources.sign_up_successful_dialog_title
import smwolczyn.composeapp.generated.resources.sign_up_user_exists_dialog_message
import smwolczyn.composeapp.generated.resources.sign_up_user_exists_dialog_title
import smwolczyn.composeapp.generated.resources.user_birthday
import smwolczyn.composeapp.generated.resources.user_city
import smwolczyn.composeapp.generated.resources.user_first_name
import smwolczyn.composeapp.generated.resources.user_last_name

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
        handleAction = viewModel::handleAction,
    )
}

@Composable
private fun SignUpScreenContent(
    navigateUp: () -> Unit,
    openSignIn: (String) -> Unit,
    state: SignUpScreenState,
    handleAction: (SignUpAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val (firstNameRef, lastNameRef, cityRef, emailRef, passwordRef) = remember { FocusRequester.createRefs() }
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
                value = state.firstName,
                onValueChange = { handleAction(UpdateFirstName(it)) },
                singleLine = true,
                label = { WolczynText(text = stringResource(Res.string.user_first_name)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                isError = state.firstNameError,
                supportingText = if (state.firstNameError) {
                    {
                        WolczynText(text = stringResource(Res.string.first_name_error))
                    }
                } else null,
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .focusRequester(firstNameRef)
                    .focusProperties { next = lastNameRef },
            )

            OutlinedTextField(
                value = state.lastName,
                onValueChange = { handleAction(UpdateLastName(it)) },
                singleLine = true,
                label = { WolczynText(text = stringResource(Res.string.user_last_name)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                isError = state.lastNameError,
                supportingText = if (state.lastNameError) {
                    {
                        WolczynText(text = stringResource(Res.string.last_name_error))
                    }
                } else null,
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .focusRequester(lastNameRef)
                    .focusProperties { next = cityRef },
            )

            OutlinedTextField(
                value = state.city,
                onValueChange = { handleAction(UpdateCity(it)) },
                singleLine = true,
                label = { WolczynText(text = stringResource(Res.string.user_city)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                isError = state.cityError,
                supportingText = if (state.cityError) {
                    {
                        WolczynText(text = stringResource(Res.string.city_error))
                    }
                } else null,
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .focusRequester(cityRef)
                    .focusProperties { next = emailRef },
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { handleAction(UpdateEmail(it)) },
                singleLine = true,
                label = { WolczynText(text = stringResource(Res.string.email)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                isError = state.emailError,
                supportingText = if (state.emailError) {
                    {
                        WolczynText(text = stringResource(Res.string.email_error_invalid))
                    }
                } else null,
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .focusRequester(emailRef)
                    .focusProperties { next = passwordRef },
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { handleAction(UpdatePassword(it)) },
                singleLine = true,
                label = { WolczynText(text = stringResource(Res.string.password)) },
                visualTransformation = if (state.passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                trailingIcon = {
                    IconButton(onClick = { handleAction(TogglePasswordHidden) }) {
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
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .focusRequester(passwordRef),
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
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.error,
                ),
                isError = state.birthdayError,
                supportingText = if (state.birthdayError) {
                    {
                        WolczynText(text = stringResource(Res.string.birthday_error))
                    }
                } else null,
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .clickable { dateDialogVisible = true }
                    .fillMaxWidth(),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { handleAction(UpdateConsents(state.consentsChecked.not())) },
            ) {
                Checkbox(
                    checked = state.consentsChecked,
                    onCheckedChange = { handleAction(UpdateConsents(it)) },
                )
                WolczynText(
                    text = buildAnnotatedString {
                        stringResource(Res.string.sign_up_consents)
                            .split(DATA_PROCESSING, PRIVACY_POLICY)
                            .let {
                                append(it[0])
                                withLink(
                                    LinkAnnotation.Url(
                                        url = DATA_PROCESSING_LINK,
                                        styles = TextLinkStyles(style = SpanStyle(color = wolczynColors.primary)),
                                    )
                                ) {
                                    append(stringResource(Res.string.sign_up_data_processing))
                                }
                                append(it[1])
                                withLink(
                                    LinkAnnotation.Url(
                                        url = PRIVACY_POLICY_LINK,
                                        styles = TextLinkStyles(style = SpanStyle(color = wolczynColors.primary)),
                                    )
                                ) {
                                    append(stringResource(Res.string.sign_up_privacy_policy))
                                }
                                append(it[2])
                            }
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 8.dp),
                )
            }

            Button(
                enabled = state.consentsChecked,
                onClick = {
                    focusManager.clearFocus(true)
                    handleAction(SignUp)
                },
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth(),
            ) {
                WolczynText(text = stringResource(Res.string.create_account))
            }

            HeightSpacer(8.dp)
        }
    }

    DatePickDialog(
        isVisible = dateDialogVisible,
        dateMillis = state.birthdayDate,
        onDismiss = { dateDialogVisible = false },
        onDateSelected = { handleAction(UpdateBirthday(it)) },
    )

    LoadingDialog(visible = state.loading)

    WolczynAlertDialog(
        isVisible = state.isSignedUpDialogVisible,
        imageVector = Icons.Outlined.ManageAccounts,
        dialogTitleId = Res.string.sign_up_successful_dialog_title,
        dialogTextId = Res.string.sign_up_successful_dialog_message,
        confirmBtnTextId = Res.string.ok,
        onConfirm = {
            handleAction(ToggleSignUpSuccessDialog)
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
            handleAction(ToggleAccountExistsDialog)
            openSignIn(state.email)
        },
        dismissBtnTextId = Res.string.cancel,
        onDismissRequest = {
            handleAction(ToggleAccountExistsDialog)
        },
        dismissible = false,
    )

    NoInternetDialog(
        isVisible = state.noInternetDialogVisible,
        onReconnect = {
            handleAction(ToggleNoInternetDialog)
            handleAction(SignUp)
        },
        onDismiss = { handleAction(ToggleNoInternetDialog) },
    )
}

private const val DATA_PROCESSING = "%data_processing%"
private const val DATA_PROCESSING_LINK =
    "https://wolczyn.kapucyni.pl/przetwarzanie-danych-osobowych/"
private const val PRIVACY_POLICY = "%privacy_policy%"
private const val PRIVACY_POLICY_LINK = "https://wolczyn.kapucyni.pl/polityka-prywatnosci/"