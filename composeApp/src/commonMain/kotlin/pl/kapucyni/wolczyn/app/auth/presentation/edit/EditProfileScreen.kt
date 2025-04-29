package pl.kapucyni.wolczyn.app.auth.presentation.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.*
import pl.kapucyni.wolczyn.app.common.presentation.composables.DatePickDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDate
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_save_profile
import smwolczyn.composeapp.generated.resources.city_error
import smwolczyn.composeapp.generated.resources.edit_profile_title
import smwolczyn.composeapp.generated.resources.first_name_error
import smwolczyn.composeapp.generated.resources.last_name_error
import smwolczyn.composeapp.generated.resources.user_birthday
import smwolczyn.composeapp.generated.resources.user_city
import smwolczyn.composeapp.generated.resources.user_first_name
import smwolczyn.composeapp.generated.resources.user_last_name

@Composable
fun EditProfileScreen(
    navigateUp: () -> Unit,
    viewModel: EditProfileViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditProfileScreenContent(
        navigateUp = navigateUp,
        state = state,
        handleAction = viewModel::handleAction,
    )
}

@Composable
private fun EditProfileScreenContent(
    navigateUp: () -> Unit,
    state: EditProfileScreenState,
    handleAction: (EditProfileAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val (firstNameRef, lastNameRef, cityRef) = remember { FocusRequester.createRefs() }
    var dateDialogVisible by rememberSaveable { mutableStateOf(false) }

    ScreenLayout(
        title = stringResource(Res.string.edit_profile_title),
        onBackPressed = navigateUp,
        actionIcon = {
            IconButton(onClick = { handleAction(SaveData) }) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    tint = wolczynColors.primary,
                    contentDescription = stringResource(Res.string.cd_save_profile),
                )
            }
        },
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
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
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
                value = state.birthdayDate.getFormattedDate(),
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
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .clickable { dateDialogVisible = true }
                    .fillMaxWidth(),
            )
        }
    }

    DatePickDialog(
        isVisible = dateDialogVisible,
        dateMillis = state.birthdayDate,
        onDismiss = { dateDialogVisible = false },
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