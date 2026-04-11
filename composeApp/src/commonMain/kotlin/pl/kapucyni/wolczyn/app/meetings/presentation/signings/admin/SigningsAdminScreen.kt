package pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pl.kapucyni.wolczyn.app.common.presentation.ObserveAsEvents
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables.SigningsContent
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.HideNoInternetDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.OnBackPressed
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.RemoveSigning
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.SaveData
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateContactNumber
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateCity
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateCommunity
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateEmail
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateLastName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateNotes
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdatePesel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminAction.UpdateWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.admin.SigningsAdminEvent.NavigateUp
import pl.kapucyni.wolczyn.app.theme.AppTheme
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.edit_participant
import smwolczyn.composeapp.generated.resources.new_participant
import smwolczyn.composeapp.generated.resources.signing_save

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
            SigningsContent(
                isEditing = state.isEditing,
                email = state.email,
                onEmailChanged = { handleAction(UpdateEmail(it)) },
                emailEnabled = state.emailEnabled,
                emailError = state.emailError,
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
                community = state.community,
                onCommunityChanged = { handleAction(UpdateCommunity(it)) },
                onPeselChanged = { handleAction(UpdatePesel(it)) },
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
                saveButtonRes = Res.string.signing_save,
                onSaveClicked = { handleAction(SaveData) },
                onRemoveSigningClicked = { handleAction(RemoveSigning) },
            )
        }
    }

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
            state = SigningsAdminState(
                notesEnabled = true,
                type = ParticipantType.ANIMATOR,
            ),
            handleAction = {},
        )
    }
}