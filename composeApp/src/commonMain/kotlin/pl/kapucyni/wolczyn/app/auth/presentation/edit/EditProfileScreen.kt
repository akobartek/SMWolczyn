package pl.kapucyni.wolczyn.app.auth.presentation.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.BirthdayTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.CityTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.FirstNameTextField
import pl.kapucyni.wolczyn.app.common.presentation.composables.LastNameTextField
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.SaveData
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.ToggleNoInternetDialog
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateBirthday
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateCity
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateFirstName
import pl.kapucyni.wolczyn.app.auth.presentation.edit.EditProfileAction.UpdateLastName
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.NoInternetDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_save_profile
import smwolczyn.composeapp.generated.resources.edit_profile_title

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
    val focusManager = LocalFocusManager.current
    val (firstNameRef, lastNameRef, cityRef, birthdayRef) =
        remember { FocusRequester.createRefs() }

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
            FirstNameTextField(
                value = state.firstName,
                onValueChange = { handleAction(UpdateFirstName(it)) },
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                error = state.firstNameError,
                modifier = Modifier
                    .focusRequester(firstNameRef)
                    .focusProperties { next = lastNameRef },
            )

            LastNameTextField(
                value = state.lastName,
                onValueChange = { handleAction(UpdateLastName(it)) },
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                error = state.lastNameError,
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
        }
    }

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