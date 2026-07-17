package pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.BirthdayTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.CityTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmailTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.FirstNameTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.LastNameTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.PeselTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.PhoneNumberTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.SelectableTextView
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.domain.model.ParticipantType
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.community
import smwolczyn.composeapp.generated.resources.community_placeholder
import smwolczyn.composeapp.generated.resources.email_error_invalid
import smwolczyn.composeapp.generated.resources.ic_construction
import smwolczyn.composeapp.generated.resources.ic_delete
import smwolczyn.composeapp.generated.resources.ic_follow_the_signs
import smwolczyn.composeapp.generated.resources.meeting_signing_remove
import smwolczyn.composeapp.generated.resources.meeting_signing_remove_dialog_btn
import smwolczyn.composeapp.generated.resources.meeting_signing_remove_dialog_message
import smwolczyn.composeapp.generated.resources.meeting_signing_remove_dialog_title
import smwolczyn.composeapp.generated.resources.notes_animator
import smwolczyn.composeapp.generated.resources.notes_animator_error
import smwolczyn.composeapp.generated.resources.notes_animator_placeholder
import smwolczyn.composeapp.generated.resources.notes_general
import smwolczyn.composeapp.generated.resources.notes_general_error
import smwolczyn.composeapp.generated.resources.participant_type
import smwolczyn.composeapp.generated.resources.participant_type_error
import smwolczyn.composeapp.generated.resources.phone_number_error
import smwolczyn.composeapp.generated.resources.workshops
import smwolczyn.composeapp.generated.resources.workshops_error

@Composable
fun SigningsContent(
    isEditing: Boolean,
    email: String,
    onEmailChanged: (String) -> Unit = {},
    emailEnabled: Boolean = false,
    emailError: Boolean = false,
    firstName: String,
    firstNameError: Boolean,
    onFirstNameChanged : (String) -> Unit,
    lastName: String,
    lastNameError: Boolean,
    onLastNameChanged : (String) -> Unit,
    city: String,
    cityError: Boolean,
    onCityChanged : (String) -> Unit,
    contactNumber: String,
    contactNumberError: Boolean,
    onContactNumberChanged : (String) -> Unit,
    pesel: String,
    peselError: Boolean,
    onPeselChanged : (String) -> Unit,
    community: String,
    onCommunityChanged : (String) -> Unit,
    birthdayDate: Long?,
    birthdayError: Boolean,
    onBirthdaySelected : (Long) -> Unit,
    availableTypes: List<ParticipantType>,
    type: ParticipantType?,
    typeError: Boolean,
    onTypeSelected : (ParticipantType) -> Unit,
    availableWorkshops: List<String>,
    selectedWorkshop: String?,
    workshopsEnabled: Boolean,
    workshopError: Boolean,
    onWorkshopSelected : (String) -> Unit,
    notes: String,
    notesEnabled: Boolean,
    notesError: Boolean,
    onNotesChanged : (String) -> Unit,
    consents: @Composable () -> Unit = {},
    saveEnabled: Boolean = true,
    saveButtonRes: StringResource,
    onSaveClicked: () -> Unit,
    onRemoveSigningClicked: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val (emailRef, firstNameRef, lastNameRef, cityRef, phoneNumberRef, birthdayRef, peselRef, communityRef, typeRef) =
        remember { FocusRequester.createRefs() }
    var removeSigningDialogVisible by remember { mutableStateOf(false) }

    EmailTextField(
        value = email,
        onValueChange = onEmailChanged,
        enabled = emailEnabled,
        errorMessage =
            if (emailError) stringResource(Res.string.email_error_invalid)
            else null,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) },
        ),
        modifier = Modifier
            .focusRequester(emailRef)
            .focusProperties { next = firstNameRef },
    )

    FirstNameTextField(
        value = firstName,
        onValueChange = onFirstNameChanged,
        error = firstNameError,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) },
        ),
        modifier = Modifier
            .focusRequester(firstNameRef)
            .focusProperties { next = lastNameRef },
    )

    LastNameTextField(
        value = lastName,
        onValueChange = onLastNameChanged,
        error = lastNameError,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) },
        ),
        modifier = Modifier
            .focusRequester(lastNameRef)
            .focusProperties { next = cityRef },
    )

    CityTextField(
        value = city,
        onValueChange = onCityChanged,
        error = cityError,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) },
        ),
        modifier = Modifier
            .focusRequester(cityRef)
            .focusProperties { next = phoneNumberRef },
    )

    PhoneNumberTextField(
        value = contactNumber,
        onValueChange = onContactNumberChanged,
        errorMessage =
            if (contactNumberError) stringResource(Res.string.phone_number_error)
            else null,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) },
        ),
        modifier = Modifier
            .focusRequester(phoneNumberRef)
            .focusProperties { next = birthdayRef },
    )

    BirthdayTextField(
        value = birthdayDate,
        onDateSelected = onBirthdaySelected,
        error = birthdayError,
        modifier = Modifier.focusRequester(birthdayRef),
    )

    PeselTextField(
        value = pesel,
        onValueChange = onPeselChanged,
        error = peselError,
        enabled = birthdayDate != null,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) },
        ),
        modifier = Modifier
            .focusRequester(peselRef)
            .focusProperties { next = communityRef },
    )

    OutlinedTextField(
        value = community,
        onValueChange = onCommunityChanged,
        label = { WolczynText(text = stringResource(Res.string.community)) },
        placeholder = { WolczynText(text = stringResource(Res.string.community_placeholder)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Next) },
        ),
        singleLine = true,
        modifier = Modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth()
            .focusRequester(communityRef)
            .focusProperties { next = typeRef },
    )

    SelectableTextView(
        value = type?.let { stringResource(it.stringRes) }.orEmpty(),
        label = Res.string.participant_type,
        enabled = birthdayDate != null,
        items = availableTypes.map { it to stringResource(it.stringRes) },
        onItemSelected = onTypeSelected,
        leadingIcon = vectorResource(Res.drawable.ic_follow_the_signs),
        error = if (typeError) Res.string.participant_type_error else null,
        modifier = Modifier.focusRequester(typeRef),
    )

    ParticipantNotesTextField(
        value = notes,
        onValueChange = onNotesChanged,
        label = when (type) {
            ParticipantType.ANIMATOR -> Res.string.notes_animator
            else -> Res.string.notes_general
        },
        placeholder = when (type) {
            ParticipantType.ANIMATOR -> Res.string.notes_animator_placeholder
            else -> Res.string.notes_general
        },
        errorMessage = when(type) {
            ParticipantType.ANIMATOR -> Res.string.notes_animator_error
            else -> Res.string.notes_general_error
        }.takeIf { notesError },
        enabled = notesEnabled,
    )

    AnimatedVisibility(workshopsEnabled) {
        SelectableTextView(
            value = selectedWorkshop.orEmpty(),
            label = Res.string.workshops,
            items = availableWorkshops.map { it to it },
            onItemSelected = onWorkshopSelected,
            leadingIcon = vectorResource(Res.drawable.ic_construction),
            error = if (workshopError) Res.string.workshops_error else null,
        )
    }

    consents()

    Button(
        onClick = {
            focusManager.clearFocus(true)
            onSaveClicked()
        },
        enabled = saveEnabled,
        modifier = Modifier
            .padding(top = 8.dp)
            .widthIn(max = 420.dp)
            .fillMaxWidth(),
    ) {
        WolczynText(text = stringResource(saveButtonRes))
    }

    if (isEditing)
        OutlinedButton(
            onClick = {
                focusManager.clearFocus(true)
                onRemoveSigningClicked()
            },
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth(0.75f),
        ) {
            WolczynText(text = stringResource(Res.string.meeting_signing_remove))
        }

    HeightSpacer(12.dp)

    WolczynAlertDialog(
        isVisible = removeSigningDialogVisible,
        imageVector = vectorResource(Res.drawable.ic_delete),
        dialogTitleId = Res.string.meeting_signing_remove_dialog_title,
        dialogTextId = Res.string.meeting_signing_remove_dialog_message,
        confirmBtnTextId = Res.string.meeting_signing_remove_dialog_btn,
        onConfirm = onRemoveSigningClicked,
        dismissBtnTextId = Res.string.cancel,
        onDismissRequest = { removeSigningDialogVisible = false },
        dismissible = true,
    )
}