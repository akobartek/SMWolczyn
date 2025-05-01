package pl.kapucyni.wolczyn.app.auth.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.PersonSearch
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.SignUp
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.ToggleAccountExistsDialog
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.ToggleNoInternetDialog
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.ToggleSignUpSuccessDialog
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.UpdateCity
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.UpdateConsents
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.UpdateEmail
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.UpdateLastName
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpAction.UpdatePassword
import pl.kapucyni.wolczyn.app.auth.presentation.signup.SignUpScreenState.PasswordErrorType
import pl.kapucyni.wolczyn.app.common.presentation.composables.BirthdayTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.CheckableField
import pl.kapucyni.wolczyn.app.common.presentation.composables.CityTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmailTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.FirstNameTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.LastNameTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.PasswordTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.create_account
import smwolczyn.composeapp.generated.resources.email_error_invalid
import smwolczyn.composeapp.generated.resources.ok
import smwolczyn.composeapp.generated.resources.password_error_too_short
import smwolczyn.composeapp.generated.resources.password_error_wrong
import smwolczyn.composeapp.generated.resources.sign_in
import smwolczyn.composeapp.generated.resources.sign_up
import smwolczyn.composeapp.generated.resources.sign_up_consents
import smwolczyn.composeapp.generated.resources.sign_up_data_processing
import smwolczyn.composeapp.generated.resources.sign_up_privacy_policy
import smwolczyn.composeapp.generated.resources.sign_up_successful_dialog_message
import smwolczyn.composeapp.generated.resources.sign_up_successful_dialog_title
import smwolczyn.composeapp.generated.resources.sign_up_user_exists_dialog_message
import smwolczyn.composeapp.generated.resources.sign_up_user_exists_dialog_title

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
    val (firstNameRef, lastNameRef, cityRef, emailRef, passwordRef, birthdayRef) =
        remember { FocusRequester.createRefs() }

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
            FirstNameTextField(
                value = state.firstName,
                onValueChange = { handleAction(UpdateFirstName(it)) },
                error = state.firstNameError,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                modifier = Modifier
                    .focusRequester(firstNameRef)
                    .focusProperties { next = lastNameRef },
            )

            LastNameTextField(
                value = state.lastName,
                onValueChange = { handleAction(UpdateLastName(it)) },
                error = state.lastNameError,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                modifier = Modifier
                    .focusRequester(lastNameRef)
                    .focusProperties { next = cityRef },
            )

            CityTextField(
                value = state.city,
                onValueChange = { handleAction(UpdateCity(it)) },
                error = state.cityError,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                modifier = Modifier
                    .focusRequester(cityRef)
                    .focusProperties { next = emailRef },
            )

            EmailTextField(
                value = state.email,
                onValueChange = { handleAction(UpdateEmail(it)) },
                errorMessage =
                    if (state.birthdayError) stringResource(Res.string.email_error_invalid)
                    else null,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                modifier = Modifier
                    .focusRequester(emailRef)
                    .focusProperties { next = passwordRef },
            )

            PasswordTextField(
                value = state.password,
                onValueChange = { handleAction(UpdatePassword(it)) },
                errorMessage = state.passwordError?.let {
                    stringResource(
                        when (state.passwordError) {
                            PasswordErrorType.TOO_SHORT -> Res.string.password_error_too_short
                            PasswordErrorType.WRONG -> Res.string.password_error_wrong
                        }
                    )
                },
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                modifier = Modifier
                    .focusRequester(passwordRef)
                    .focusProperties { next = birthdayRef },
            )

            BirthdayTextField(
                value = state.birthdayDate,
                onDateSelected = { handleAction(UpdateBirthday(it)) },
                error = state.birthdayError,
                modifier = Modifier.focusRequester(birthdayRef),
            )

            CheckableField(
                checked = state.consentsChecked,
                onCheckedChange = { handleAction(UpdateConsents(it)) },
                text = buildConsentsString(),
            )

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

@Composable
private fun buildConsentsString() = buildAnnotatedString {
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
}

private const val DATA_PROCESSING = "%data_processing%"
private const val DATA_PROCESSING_LINK =
    "https://wolczyn.kapucyni.pl/przetwarzanie-danych-osobowych/"
private const val PRIVACY_POLICY = "%privacy_policy%"
private const val PRIVACY_POLICY_LINK = "https://wolczyn.kapucyni.pl/polityka-prywatnosci/"