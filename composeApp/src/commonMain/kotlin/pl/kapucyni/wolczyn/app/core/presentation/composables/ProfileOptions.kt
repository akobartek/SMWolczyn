package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.LockReset
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.DeleteAccount
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.ResetPassword
import pl.kapucyni.wolczyn.app.auth.presentation.AuthAction.SignOut
import pl.kapucyni.wolczyn.app.common.presentation.Screen
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynAlertDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.delete
import smwolczyn.composeapp.generated.resources.delete_account
import smwolczyn.composeapp.generated.resources.delete_account_dialog_msg
import smwolczyn.composeapp.generated.resources.delete_account_dialog_title
import smwolczyn.composeapp.generated.resources.edit_profile_title
import smwolczyn.composeapp.generated.resources.message_sent
import smwolczyn.composeapp.generated.resources.ok
import smwolczyn.composeapp.generated.resources.reset_password
import smwolczyn.composeapp.generated.resources.reset_password_dialog_msg
import smwolczyn.composeapp.generated.resources.sign_out

@Composable
fun ProfileOptions(
    user: User?,
    navigate: (Screen) -> Unit,
    handleAuthAction: (AuthAction) -> Unit,
) {
    var dropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var resetPasswordDialogVisible by remember { mutableStateOf(false) }
    var deleteAccountDialogVisible by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = {
            user?.let { dropdownExpanded = true } ?: navigate(Screen.SignIn())
        }) {
            ProfilePicture(user = user)
        }

        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
            modifier = Modifier.defaultMinSize(minWidth = 200.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                ProfilePicture(user = user)
                Spacer(modifier = Modifier.width(12.dp))
                WolczynText(text = user?.email ?: "")
            }
            HorizontalDivider()
            Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                DropdownMenuItem(
                    text = { WolczynText(text = stringResource(Res.string.edit_profile_title)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.ManageAccounts,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        dropdownExpanded = false
                        navigate(Screen.EditProfile)
                    },
                )
                DropdownMenuItem(
                    text = { WolczynText(text = stringResource(Res.string.sign_out)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        dropdownExpanded = false
                        handleAuthAction(SignOut)
                    },
                )
                DropdownMenuItem(
                    text = { WolczynText(text = stringResource(Res.string.reset_password)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.LockReset,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        dropdownExpanded = false
                        resetPasswordDialogVisible = true
                    },
                )
                DropdownMenuItem(
                    text = { WolczynText(text = stringResource(Res.string.delete_account)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.NoAccounts,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        dropdownExpanded = false
                        deleteAccountDialogVisible = true
                    },
                )
            }
        }
    }

    WolczynAlertDialog(
        isVisible = resetPasswordDialogVisible,
        imageVector = Icons.Outlined.LockReset,
        dialogTitleId = Res.string.message_sent,
        dialogTextId = Res.string.reset_password_dialog_msg,
        confirmBtnTextId = Res.string.ok,
        onConfirm = {
            handleAuthAction(ResetPassword)
            resetPasswordDialogVisible = false
        },
        onDismissRequest = { resetPasswordDialogVisible = false },
    )

    WolczynAlertDialog(
        isVisible = deleteAccountDialogVisible,
        imageVector = Icons.Outlined.NoAccounts,
        dialogTitleId = Res.string.delete_account_dialog_title,
        dialogTextId = Res.string.delete_account_dialog_msg,
        confirmBtnTextId = Res.string.delete,
        onConfirm = {
            handleAuthAction(DeleteAccount)
            deleteAccountDialogVisible = false
        },
        dismissBtnTextId = Res.string.cancel,
        onDismissRequest = { deleteAccountDialogVisible = false },
    )
}