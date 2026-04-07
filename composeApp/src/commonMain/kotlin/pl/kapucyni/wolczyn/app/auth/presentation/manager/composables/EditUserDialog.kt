package pl.kapucyni.wolczyn.app.auth.presentation.manager.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.model.UserPermit
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.theme.AppTheme
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.ic_edit
import smwolczyn.composeapp.generated.resources.save
import smwolczyn.composeapp.generated.resources.user_permits_title

@Composable
fun EditUserDialog(
    isVisible: Boolean,
    user: User,
    onSave: (User) -> Unit,
    onCancel: () -> Unit,
) {
    if (isVisible) {
        var selectedType by rememberSaveable { mutableStateOf(user.userType) }
        var permits = remember { user.permits.toMutableStateList() }

        AlertDialog(
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_edit),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
            title = {
                WolczynText(
                    text = "${user.firstName} ${user.lastName}",
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    UserType.entries.filter { it != UserType.ADMIN }.forEach { type ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    selectedType = type
                                    permits =
                                        (if (type.allowPermits().not()) listOf()
                                        else user.permits).toMutableStateList()
                                }
                                .background(
                                    color =
                                        if (selectedType == type)
                                            MaterialTheme.colorScheme.secondaryContainer
                                        else MaterialTheme.colorScheme.background,
                                )
                                .padding(8.dp),
                        ) {
                            WolczynText(
                                text = stringResource(type.stringRes),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    textAlign = TextAlign.Center,
                                    color =
                                        if (selectedType == type)
                                            MaterialTheme.colorScheme.onSecondaryContainer
                                        else MaterialTheme.colorScheme.onBackground,
                                ),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                    AnimatedVisibility(visible = selectedType.allowPermits()) {
                        Column {
                            WolczynText(
                                text = stringResource(Res.string.user_permits_title),
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                            )
                            FlowRow(
                                horizontalArrangement = Arrangement.Center,
                                maxItemsInEachRow = 2,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                UserPermit.entries.forEach { permit ->
                                    val selected = permits.contains(permit)
                                    FilterChip(
                                        label = {
                                            WolczynText(
                                                text = stringResource(permit.stringRes),
                                                textStyle = MaterialTheme.typography.bodySmall,
                                            )
                                        },
                                        selected = selected,
                                        onClick = {
                                            if (selected) permits.remove(permit)
                                            else permits.add(permit)
                                        },
                                        modifier = Modifier.padding(horizontal = 4.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            },
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    onSave(user.copy(userType = selectedType, permits = permits))
                }) {
                    WolczynText(stringResource(Res.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    WolczynText(stringResource(Res.string.cancel))
                }
            },
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = AndroidUiModes.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun EditUserDialogPreview() {
    AppTheme {
        EditUserDialog(
            isVisible = true,
            user = User(
                id = "id",
                publicId = 2137,
                email = "test@test.com",
                userType = UserType.ORGANISATION,
                firstName = "Test",
                lastName = "Testowy",
                city = "Kraków",
                permits = listOf(UserPermit.SIGNINGS),
            ),
            onSave = {},
            onCancel = {},
        )
    }
}