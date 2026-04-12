package pl.kapucyni.wolczyn.app.meetings.presentation.signings.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.gitlive.firebase.firestore.Timestamp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import pl.kapucyni.wolczyn.app.common.presentation.ObserveAsEvents
import pl.kapucyni.wolczyn.app.common.presentation.composables.CheckableField
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.buildLinkableString
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables.SigningsContent
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.HideNoInternetDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.HideSuccessDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.HideTooYoungDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.RemoveSigning
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.SaveData
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateAdditionalInfoChecked
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateContactNumber
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateCity
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateCommunity
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateLastName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateNotes
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdatePesel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateStatuteConsent
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsAction.UpdateWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsEvent.NavigateUp
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.SigningsEvent.UserNotAvailable
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.composables.SigningsConfirmedScreen
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.composables.SigningsNoUserDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.composables.SigningsQrCodeDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.user.composables.SigningsSubtitle
import pl.kapucyni.wolczyn.app.theme.AppTheme
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_save_signing
import smwolczyn.composeapp.generated.resources.cd_scan_signing
import smwolczyn.composeapp.generated.resources.close
import smwolczyn.composeapp.generated.resources.ic_celebration
import smwolczyn.composeapp.generated.resources.ic_error
import smwolczyn.composeapp.generated.resources.ic_help
import smwolczyn.composeapp.generated.resources.ic_qr_code
import smwolczyn.composeapp.generated.resources.meeting_signing_animator_consent
import smwolczyn.composeapp.generated.resources.meeting_signing_animator_document
import smwolczyn.composeapp.generated.resources.meeting_signing_animator_info
import smwolczyn.composeapp.generated.resources.meeting_signing_essentials
import smwolczyn.composeapp.generated.resources.meeting_signing_success_dialog_message
import smwolczyn.composeapp.generated.resources.meeting_signing_success_dialog_title
import smwolczyn.composeapp.generated.resources.meeting_signing_too_young_dialog_message
import smwolczyn.composeapp.generated.resources.meeting_signing_too_young_dialog_title
import smwolczyn.composeapp.generated.resources.meeting_signing_underage_info
import smwolczyn.composeapp.generated.resources.meeting_statute_title
import smwolczyn.composeapp.generated.resources.meeting_statute_value
import smwolczyn.composeapp.generated.resources.meeting_underage_consent
import smwolczyn.composeapp.generated.resources.signing_edit
import smwolczyn.composeapp.generated.resources.signing_send
import smwolczyn.composeapp.generated.resources.signings

@Composable
fun SigningsScreen(
    navigateUp: () -> Unit,
    openAuth: () -> Unit,
    viewModel: SigningsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    var noUserDialogVisible by rememberSaveable { mutableStateOf(false) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is NavigateUp -> navigateUp()
            is UserNotAvailable -> { noUserDialogVisible = true }
        }
    }

    LoadingDialog(
        visible = loading,
        onDismiss = navigateUp,
    )

    state?.let {
        SigningsScreen(
            state = it,
            handleAction = viewModel::handleAction,
            navigateUp = navigateUp,
        )
    } ?: LoadingBox()

    SigningsNoUserDialog(
        isVisible = noUserDialogVisible,
        onConfirm = openAuth,
        onCancel = navigateUp,
    )
}

@Composable
private fun SigningsScreen(
    state: SigningsState,
    handleAction: (SigningsAction) -> Unit,
    navigateUp: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    var qrCodeEmail by rememberSaveable { mutableStateOf<String?>(null) }

    ScreenLayout(
        title = stringResource(Res.string.signings),
        onBackPressed = navigateUp,
        actionIcon = {
            state.qrEmail?.let { qrEmail ->
                IconButton(onClick = { qrCodeEmail = qrEmail }) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_qr_code),
                        tint = wolczynColors.primary,
                        contentDescription = stringResource(Res.string.cd_scan_signing),
                    )
                }
            }
            if (state is SigningsState.NotConfirmed && state.essentialsUrl.isNotBlank())
                IconButton(onClick = { uriHandler.openUri(state.essentialsUrl) }) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_help),
                        tint = wolczynColors.primary,
                        contentDescription = stringResource(Res.string.cd_save_signing),
                    )
                }
        },
    ) {
        when (state) {
            is SigningsState.Confirmed -> {
                SigningsConfirmedScreen(state = state)
            }

            is SigningsState.NotConfirmed -> {
                SigningsScreenContent(
                    state = state,
                    handleAction = handleAction,
                    openEssentials = {
                        state.essentialsUrl.takeIf { it.isNotBlank() }
                            ?.let { uriHandler.openUri(it) }
                    },
                )
            }
        }
    }

    SigningsQrCodeDialog(
        email = qrCodeEmail,
        onCancel = { qrCodeEmail = null },
    )
}

@Composable
private fun SigningsScreenContent(
    state: SigningsState.NotConfirmed,
    handleAction: (SigningsAction) -> Unit,
    openEssentials: () -> Unit,
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
        SigningsSubtitle(state = state)

        SigningsContent(
            isEditing = state.isEditing,
            email = state.email,
            firstName = state.firstName,
            firstNameError = state.firstNameError,
            onFirstNameChanged = { handleAction(UpdateFirstName(it)) },
            lastName = state.lastName,
            lastNameError = state.lastNameError,
            onLastNameChanged = { handleAction(UpdateLastName(it)) },
            city = state.city,
            cityError = state.cityError,
            onCityChanged = { handleAction(UpdateCity(it)) },
            contactNumber = state.contactNumber,
            contactNumberError = state.contactNumberError,
            onContactNumberChanged = { handleAction(UpdateContactNumber(it)) },
            pesel = state.pesel,
            peselError = state.peselError,
            onPeselChanged = { handleAction(UpdatePesel(it)) },
            community = state.community,
            onCommunityChanged = { handleAction(UpdateCommunity(it)) },
            birthdayDate = state.birthdayDate,
            birthdayError = state.birthdayError,
            onBirthdaySelected = { handleAction(UpdateBirthday(it)) },
            availableTypes = state.availableTypes,
            type = state.type,
            typeError = state.typeError,
            onTypeSelected = { handleAction(UpdateType(it)) },
            availableWorkshops = state.availableWorkshops,
            selectedWorkshop = state.selectedWorkshop,
            workshopsEnabled = state.workshopsEnabled,
            workshopError = state.workshopError,
            onWorkshopSelected = { handleAction(UpdateWorkshop(it)) },
            notes = state.notes,
            notesEnabled = state.notesEnabled,
            notesError = state.notesError,
            onNotesChanged = { handleAction(UpdateNotes(it)) },
            saveEnabled = state.statuteChecked && state.animatorInfoChecked(),
            saveButtonRes = when {
                state.isEditing -> Res.string.signing_edit
                else -> Res.string.signing_send
            },
            onSaveClicked = { handleAction(SaveData) },
            onRemoveSigningClicked = { handleAction(RemoveSigning) },
            consents = {
                if (state.isEditing.not())
                    CheckableField(
                        checked = state.statuteChecked,
                        onCheckedChange = { handleAction(UpdateStatuteConsent(it)) },
                        text = buildLinkableString(
                            text = Res.string.meeting_statute_title,
                            links = listOf(Triple(STATUTE, state.statuteUrl, Res.string.meeting_statute_value)),
                        ),
                    )

                if (state.type == ParticipantType.ANIMATOR) {
                    WolczynText(
                        text = buildLinkableString(
                            text = Res.string.meeting_signing_animator_info,
                            links = listOf(
                                Triple(
                                    ANIMATOR_DOCUMENT,
                                    ANIMATOR_DOCUMENT_URL,
                                    Res.string.meeting_signing_animator_document,
                                ),
                            ),
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Justify),
                    )

                    CheckableField(
                        checked = state.additionalInfoChecked,
                        onCheckedChange = { handleAction(UpdateAdditionalInfoChecked(it)) },
                        text = stringResource(Res.string.meeting_signing_animator_consent),
                    )
                }

                if (state.isUnderAge) {
                    WolczynText(
                        text = buildLinkableString(
                            text = Res.string.meeting_signing_underage_info,
                            links = listOf(
                                Triple(UNDER_AGE, state.parentAgreementUrl, Res.string.meeting_underage_consent),
                            ),
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Justify),
                    )

                    CheckableField(
                        checked = state.additionalInfoChecked,
                        onCheckedChange = { handleAction(UpdateAdditionalInfoChecked(it)) },
                        text = stringResource(Res.string.meeting_signing_animator_consent),
                    )
                }
            },
        )
    }

    WolczynAlertDialog(
        isVisible = state.successDialogVisible,
        imageVector = vectorResource(Res.drawable.ic_celebration),
        dialogTitleId = Res.string.meeting_signing_success_dialog_title,
        dialogTextId = Res.string.meeting_signing_success_dialog_message,
        confirmBtnTextId = Res.string.meeting_signing_essentials,
        onConfirm = {
            handleAction(HideSuccessDialog)
            openEssentials()
        },
        dismissBtnTextId = Res.string.close,
        onDismissRequest = { handleAction(HideSuccessDialog) },
        dismissible = false,
    )

    WolczynAlertDialog(
        isVisible = state.tooYoungDialogVisible,
        imageVector = vectorResource(Res.drawable.ic_error),
        dialogTitleId = Res.string.meeting_signing_too_young_dialog_title,
        dialogTextId = Res.string.meeting_signing_too_young_dialog_message,
        confirmBtnTextId = Res.string.close,
        onConfirm = { handleAction(HideTooYoungDialog) },
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

private fun SigningsState.NotConfirmed.animatorInfoChecked() =
    if (type == ParticipantType.ANIMATOR || isUnderAge) additionalInfoChecked
    else true

private const val STATUTE = "%statute%"
private const val UNDER_AGE = "%consent%"
private const val ANIMATOR_DOCUMENT = "%animator_document%"
private const val ANIMATOR_DOCUMENT_URL = "https://www.gov.pl/web/gov/uzyskaj-zaswiadczenie-z-krajowego-rejestru-karnego"

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = AndroidUiModes.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SigningsScreenContentPreview() {
    AppTheme {
        SigningsScreenContent(
            state = SigningsState.NotConfirmed(
                essentialsUrl = "",
                statuteUrl = "",
                parentAgreementUrl = "",
                isEditing = false,
                firstName = "Test",
                lastName = "Testowy",
                city = "Testowo",
                email = "test@test.com",
                pesel = "1234567890123",
                community = "",
                birthdayDate = Timestamp.now().seconds,
                isUnderAge = true,
                availableTypes = listOf(ParticipantType.MEMBER, ParticipantType.ANIMATOR),
                type = ParticipantType.ANIMATOR,
                allWorkshops = listOf(Workshop(name = "Piłkarskie")),
                availableWorkshops = listOf("Piłkarskie"),
                selectedWorkshop = "Piłkarskie",
                statuteChecked = false,
                additionalInfoChecked = false,
            ),
            handleAction = {},
            openEssentials = {},
        )
    }
}