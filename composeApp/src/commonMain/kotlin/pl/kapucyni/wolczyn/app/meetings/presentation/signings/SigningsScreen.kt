package pl.kapucyni.wolczyn.app.meetings.presentation.signings

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FollowTheSigns
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.BirthdayTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.CheckableField
import pl.kapucyni.wolczyn.app.common.presentation.composables.CityTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmailTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.FirstNameTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.LastNameTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.PeselTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.SelectableTextView
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.HideNoInternetDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.HideSuccessDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.SaveData
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateCity
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateEmail
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateLastName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdatePesel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateStatuteConsent
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables.SigningsSubtitle
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.cd_save_profile
import smwolczyn.composeapp.generated.resources.email_error_invalid
import smwolczyn.composeapp.generated.resources.meeting_signing_essentials
import smwolczyn.composeapp.generated.resources.meeting_signing_success_dialog_message
import smwolczyn.composeapp.generated.resources.meeting_signing_success_dialog_title
import smwolczyn.composeapp.generated.resources.meeting_statute_title
import smwolczyn.composeapp.generated.resources.meeting_statute_value
import smwolczyn.composeapp.generated.resources.participant_type
import smwolczyn.composeapp.generated.resources.participant_type_error
import smwolczyn.composeapp.generated.resources.signing_save
import smwolczyn.composeapp.generated.resources.signing_send
import smwolczyn.composeapp.generated.resources.signings
import smwolczyn.composeapp.generated.resources.workshops
import smwolczyn.composeapp.generated.resources.workshops_error

@Composable
fun SigningsScreen(
    navigateUp: () -> Unit,
    viewModel: SigningsViewModel,
) {
    val uriHandler = LocalUriHandler.current
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        if ((state as? State.Success)?.data?.saveSuccess == true)
            navigateUp()
    }

    ScreenLayout(
        title = stringResource(Res.string.signings),
        onBackPressed = navigateUp,
        actionIcon = (state as? State.Success)?.let {
            {
                IconButton(onClick = { uriHandler.openUri(ESSENTIALS_LINK) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                        tint = wolczynColors.primary,
                        contentDescription = stringResource(Res.string.cd_save_profile),
                    )
                }
            }
        },
    ) {
        when (state) {
            is State.Loading -> LoadingBox()
            is State.Success -> (state as? State.Success)?.let {
                SigningsScreenContent(
                    state = it.data,
                    handleAction = viewModel::handleAction,
                    navigateUp = navigateUp,
                    openEssentials = { uriHandler.openUri(ESSENTIALS_LINK) },
                )
            } ?: LoadingBox()
        }
    }
}

@Composable
private fun SigningsScreenContent(
    state: SigningsScreenState,
    handleAction: (SigningsAction) -> Unit,
    navigateUp: () -> Unit,
    openEssentials: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val (firstNameRef, lastNameRef, cityRef, emailRef, birthdayRef, peselRef, typeRef) =
        remember { FocusRequester.createRefs() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        if (state.isUserInfoEditable.not()) {
            SigningsSubtitle(state = state)
            HeightSpacer(4.dp)
        } else {
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
                    .focusProperties { next = birthdayRef },
            )

            BirthdayTextField(
                value = state.birthdayDate,
                onDateSelected = { handleAction(UpdateBirthday(it)) },
                error = state.birthdayError,
                modifier = Modifier.focusRequester(birthdayRef),
            )
        }

        PeselTextField(
            value = state.pesel,
            onValueChange = { handleAction(UpdatePesel(it)) },
            error = state.peselError,
            visible = state.birthdayDate != null,
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
            items = state.availableTypes.map { it to stringResource(it.stringRes) },
            onItemSelected = { handleAction(UpdateType(it)) },
            leadingIcon = Icons.AutoMirrored.Filled.FollowTheSigns,
            error = if (state.typeError) Res.string.participant_type_error else null,
            modifier = Modifier.focusRequester(typeRef),
        )

        AnimatedVisibility(state.type?.canSelectWorkshops() ?: false) {
            SelectableTextView(
                value = state.selectedWorkshop.orEmpty(),
                label = Res.string.workshops,
                items = state.availableWorkshops.map { it to it },
                onItemSelected = { handleAction(UpdateWorkshop(it)) },
                leadingIcon = Icons.Default.Construction,
                error = if (state.workshopError) Res.string.workshops_error else null,
            )
        }

        CheckableField(
            checked = state.statuteChecked,
            onCheckedChange = { handleAction(UpdateStatuteConsent(it)) },
            text = buildStatuteString(),
        )

        Button(
            onClick = {
                focusManager.clearFocus(true)
                handleAction(SaveData)
            },
            enabled = state.statuteChecked,
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth(),
        ) {
            WolczynText(
                text = stringResource(
                    if (state.isEditing) Res.string.signing_save
                    else Res.string.signing_send
                ),
            )
        }
    }

    LoadingDialog(visible = state.loading)

    WolczynAlertDialog(
        isVisible = state.successDialogVisible,
        imageVector = Icons.Outlined.Celebration,
        dialogTitleId = Res.string.meeting_signing_success_dialog_title,
        dialogTextId = Res.string.meeting_signing_success_dialog_message,
        confirmBtnTextId = Res.string.meeting_signing_essentials,
        onConfirm = {
            handleAction(HideSuccessDialog)
            navigateUp()
            openEssentials()
        },
        dismissBtnTextId = Res.string.cancel,
        onDismissRequest = {
            handleAction(HideSuccessDialog)
            navigateUp()
        },
        dismissible = false,
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

@Composable
private fun buildStatuteString() = buildAnnotatedString {
    stringResource(Res.string.meeting_statute_title)
        .split(STATUTE)
        .let {
            append(it[0])
            withLink(
                LinkAnnotation.Url(
                    url = STATUTE_LINK,
                    styles = TextLinkStyles(style = SpanStyle(color = wolczynColors.primary)),
                )
            ) {
                append(stringResource(Res.string.meeting_statute_value))
            }
            append(it[1])
        }
}

private const val ESSENTIALS_LINK = "https://wolczyn.kapucyni.pl/niezbednik/"
private const val STATUTE = "%statute%"
private const val STATUTE_LINK =
    "https://wolczyn.kapucyni.pl/wp-content/uploads/2025/03/Regulamin-Spotkania.pdf"
