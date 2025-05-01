package pl.kapucyni.wolczyn.app.meetings.presentation.signings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FollowTheSigns
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.DatePickDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDate
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.SaveData
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.ToggleNoInternetDialog
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateCity
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateEmail
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateLastName
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdatePesel
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateType
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.SigningsAction.UpdateWorkshop
import pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables.SigningsSubtitle
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.birthday_error
import smwolczyn.composeapp.generated.resources.city_error
import smwolczyn.composeapp.generated.resources.email
import smwolczyn.composeapp.generated.resources.email_error_invalid
import smwolczyn.composeapp.generated.resources.first_name_error
import smwolczyn.composeapp.generated.resources.last_name_error
import smwolczyn.composeapp.generated.resources.participant_type
import smwolczyn.composeapp.generated.resources.participant_type_error
import smwolczyn.composeapp.generated.resources.pesel
import smwolczyn.composeapp.generated.resources.pesel_error
import smwolczyn.composeapp.generated.resources.signing_save
import smwolczyn.composeapp.generated.resources.signing_send
import smwolczyn.composeapp.generated.resources.signings
import smwolczyn.composeapp.generated.resources.user_birthday
import smwolczyn.composeapp.generated.resources.user_city
import smwolczyn.composeapp.generated.resources.user_first_name
import smwolczyn.composeapp.generated.resources.user_last_name
import smwolczyn.composeapp.generated.resources.workshops
import smwolczyn.composeapp.generated.resources.workshops_error

@Composable
fun SigningsScreen(
    navigateUp: () -> Unit,
    viewModel: SigningsViewModel,
) {
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

            }
        },
    ) {
        when (state) {
            is State.Loading -> LoadingBox()
            is State.Success -> (state as? State.Success)?.let {
                SigningsScreenContent(
                    state = it.data,
                    handleAction = viewModel::handleAction,
                )
            } ?: LoadingBox()
        }
    }
}

@Composable
private fun SigningsScreenContent(
    state: SigningsScreenState,
    handleAction: (SigningsAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val (firstNameRef, lastNameRef, cityRef, emailRef, birthdayRef, peselRef, typeRef) =
        remember { FocusRequester.createRefs() }
    var dateDialogVisible by rememberSaveable { mutableStateOf(false) }
    var typeDropDownVisible by rememberSaveable { mutableStateOf(false) }
    var workshopsDropDownVisible by rememberSaveable { mutableStateOf(false) }

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
                    .focusRequester(cityRef),
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
                    .focusProperties { next = birthdayRef },
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
                readOnly = true,
                singleLine = true,
                isError = state.birthdayError,
                supportingText = if (state.birthdayError) {
                    {
                        WolczynText(text = stringResource(Res.string.birthday_error))
                    }
                } else null,
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) dateDialogVisible = true
                    }
                    .focusRequester(birthdayRef),
            )
        }

        AnimatedVisibility(state.birthdayDate != null) {
            OutlinedTextField(
                value = state.pesel,
                onValueChange = { handleAction(UpdatePesel(it)) },
                singleLine = true,
                label = { WolczynText(text = stringResource(Res.string.pesel)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                isError = state.peselError,
                supportingText = if (state.peselError) {
                    {
                        WolczynText(text = stringResource(Res.string.pesel_error))
                    }
                } else null,
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .focusRequester(peselRef)
                    .focusProperties { next = typeRef },
            )
        }

        BoxWithConstraints(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = state.type?.let { stringResource(it.stringRes) }.orEmpty(),
                onValueChange = {},
                label = { WolczynText(stringResource(Res.string.participant_type)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FollowTheSigns,
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector =
                            if (typeDropDownVisible) Icons.Default.ArrowDropUp
                            else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                },
                readOnly = true,
                singleLine = true,
                isError = state.typeError,
                supportingText = if (state.typeError) {
                    {
                        WolczynText(text = stringResource(Res.string.participant_type_error))
                    }
                } else null,
                modifier = Modifier
                    .onFocusChanged {
                        if (it.isFocused) typeDropDownVisible = true
                    }
                    .fillMaxWidth()
                    .focusRequester(typeRef),
            )

            DropdownMenu(
                expanded = typeDropDownVisible,
                onDismissRequest = {
                    focusManager.clearFocus()
                    typeDropDownVisible = false
                },
                modifier = Modifier.width(maxWidth),
            ) {
                state.availableTypes.forEach {
                    DropdownMenuItem(
                        text = { WolczynText(text = stringResource(it.stringRes)) },
                        onClick = {
                            focusManager.clearFocus()
                            typeDropDownVisible = false
                            handleAction(UpdateType(it))
                        },
                    )
                }
            }
        }

        AnimatedVisibility(state.workshopsVisible) {
            BoxWithConstraints(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = state.selectedWorkshop.orEmpty(),
                    onValueChange = {},
                    label = { WolczynText(stringResource(Res.string.workshops)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Construction,
                            contentDescription = null,
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector =
                                if (typeDropDownVisible) Icons.Default.ArrowDropUp
                                else Icons.Default.ArrowDropDown,
                            contentDescription = null,
                        )
                    },
                    readOnly = true,
                    singleLine = true,
                    isError = state.workshopError,
                    supportingText = if (state.workshopError) {
                        {
                            WolczynText(text = stringResource(Res.string.workshops_error))
                        }
                    } else null,
                    modifier = Modifier
                        .onFocusChanged {
                            if (it.isFocused) workshopsDropDownVisible = true
                        }
                        .fillMaxWidth(),
                )

                DropdownMenu(
                    expanded = workshopsDropDownVisible,
                    onDismissRequest = {
                        focusManager.clearFocus()
                        workshopsDropDownVisible = false
                    },
                    modifier = Modifier.width(maxWidth),
                ) {
                    state.availableWorkshops.forEach {
                        DropdownMenuItem(
                            text = { WolczynText(text = it) },
                            onClick = {
                                focusManager.clearFocus()
                                workshopsDropDownVisible = false
                                handleAction(UpdateWorkshop(it))
                            },
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                focusManager.clearFocus(true)
                handleAction(SaveData)
            },
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

    DatePickDialog(
        isVisible = dateDialogVisible,
        dateMillis = state.birthdayDate,
        onDismiss = {
            focusManager.clearFocus()
            dateDialogVisible = false
        },
        onDateSelected = { handleAction(UpdateBirthday(it)) },
    )

    LoadingDialog(visible = state.loading)

    NoInternetDialog(
        isVisible = state.noInternetDialogVisible,
        onReconnect = {
            handleAction(ToggleNoInternetDialog)
            handleAction(SaveData)
        },
        onDismiss = { handleAction(ToggleNoInternetDialog) },
    )
}
