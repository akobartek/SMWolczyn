package pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component3
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component4
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component5
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component6
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component7
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import pl.kapucyni.wolczyn.app.common.presentation.ObserveAsEvents
import pl.kapucyni.wolczyn.app.common.presentation.composables.BirthdayTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.CityTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmailTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.FirstNameTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.LastNameTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.PeselTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.SelectableTextView
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.OnBackPressed
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.HideNoInternetDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.RemoveSigning
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.SaveData
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateCity
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateEmail
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateLastName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdatePesel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminEvent.NavigateUp
import pl.kapucyni.wolczyn.app.theme.AppTheme
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.edit_participant
import smwolczyn.composeapp.generated.resources.email_error_invalid
import smwolczyn.composeapp.generated.resources.ic_construction
import smwolczyn.composeapp.generated.resources.ic_delete
import smwolczyn.composeapp.generated.resources.ic_follow_the_signs
import smwolczyn.composeapp.generated.resources.meeting_signing_remove
import smwolczyn.composeapp.generated.resources.meeting_signing_remove_dialog_btn
import smwolczyn.composeapp.generated.resources.meeting_signing_remove_dialog_message
import smwolczyn.composeapp.generated.resources.meeting_signing_remove_dialog_title
import smwolczyn.composeapp.generated.resources.new_participant
import smwolczyn.composeapp.generated.resources.participant_type
import smwolczyn.composeapp.generated.resources.participant_type_error
import smwolczyn.composeapp.generated.resources.signing_save
import smwolczyn.composeapp.generated.resources.workshops
import smwolczyn.composeapp.generated.resources.workshops_error

@Composable
fun SigningsAdminScreen(
    navigateUp: () -> Unit,
    viewModel: SigningsAdminViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is NavigateUp -> navigateUp()
        }
    }

    LoadingDialog(
        visible = loading,
        onDismiss = navigateUp,
    )

    state?.let {
        SigningsAdminScreenContent(
            state = it,
            handleAction = viewModel::handleAction,
        )
    } ?: LoadingBox()
}

@Composable
private fun SigningsAdminScreenContent(
    state: SigningsAdminState,
    handleAction: (SigningsAdminAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val (emailRef, firstNameRef, lastNameRef, cityRef, birthdayRef, peselRef, typeRef) =
        remember { FocusRequester.createRefs() }
    var removeSigningDialogVisible by remember { mutableStateOf(false) }

    ScreenLayout(
        title = stringResource(
            if (state.isEditing) Res.string.edit_participant
            else Res.string.new_participant
        ),
        onBackPressed = { handleAction(OnBackPressed) },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 420.dp)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            EmailTextField(
                value = state.email,
                onValueChange = { handleAction(UpdateEmail(it)) },
                enabled = state.emailEnabled,
                errorMessage =
                    if (state.emailError) stringResource(Res.string.email_error_invalid)
                    else null,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                modifier = Modifier
                    .focusRequester(emailRef)
                    .focusProperties { next = firstNameRef },
            )

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
                    .focusProperties { next = birthdayRef },
            )

            BirthdayTextField(
                value = state.birthdayDate,
                onDateSelected = { handleAction(UpdateBirthday(it)) },
                error = state.birthdayError,
                modifier = Modifier.focusRequester(birthdayRef),
            )

            PeselTextField(
                value = state.pesel,
                onValueChange = { handleAction(UpdatePesel(it)) },
                error = state.peselError,
                enabled = state.birthdayDate != null,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                modifier = Modifier
                    .focusRequester(peselRef)
                    .focusProperties { next = typeRef },
            )

            SelectableTextView(
                value = state.type?.let { stringResource(it.stringRes) }.orEmpty(),
                label = Res.string.participant_type,
                enabled = state.birthdayDate != null,
                items = state.availableTypes.map { it to stringResource(it.stringRes) },
                onItemSelected = { handleAction(UpdateType(it)) },
                leadingIcon = vectorResource(Res.drawable.ic_follow_the_signs),
                error = if (state.typeError) Res.string.participant_type_error else null,
                modifier = Modifier.focusRequester(typeRef),
            )

            AnimatedVisibility(state.workshopsEnabled) {
                SelectableTextView(
                    value = state.selectedWorkshop.orEmpty(),
                    label = Res.string.workshops,
                    items = state.availableWorkshops.map { it to it },
                    onItemSelected = { handleAction(UpdateWorkshop(it)) },
                    leadingIcon = vectorResource(Res.drawable.ic_construction),
                    error = if (state.workshopError) Res.string.workshops_error else null,
                )
            }

            Button(
                onClick = {
                    focusManager.clearFocus(true)
                    handleAction(SaveData)
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
            ) {
                WolczynText(
                    text = stringResource(Res.string.signing_save),
                )
            }

            if (state.isEditing)
                OutlinedButton(
                    onClick = {
                        focusManager.clearFocus(true)
                        removeSigningDialogVisible = true
                    },
                    modifier = Modifier.fillMaxWidth(0.75f),
                ) {
                    WolczynText(text = stringResource(Res.string.meeting_signing_remove))
                }
        }
    }

    WolczynAlertDialog(
        isVisible = removeSigningDialogVisible,
        imageVector = vectorResource(Res.drawable.ic_delete),
        dialogTitleId = Res.string.meeting_signing_remove_dialog_title,
        dialogTextId = Res.string.meeting_signing_remove_dialog_message,
        confirmBtnTextId = Res.string.meeting_signing_remove_dialog_btn,
        onConfirm = { handleAction(RemoveSigning) },
        dismissBtnTextId = Res.string.cancel,
        onDismissRequest = { removeSigningDialogVisible = false },
        dismissible = true,
    )

    NoInternetDialog(
        isVisible = state.noInternetDialogVisible,
        onReconnect = {
            handleAction(HideNoInternetDialog)
            handleAction(SaveData)
        },
        onDismiss = { handleAction(HideNoInternetDialog) },
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = AndroidUiModes.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SigningsAdminScreenContentPreview() {
    AppTheme {
        SigningsAdminScreenContent(
            state = SigningsAdminState(),
            handleAction = {},
        )
    }
}