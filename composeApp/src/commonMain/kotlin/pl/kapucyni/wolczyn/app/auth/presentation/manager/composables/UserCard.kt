package pl.kapucyni.wolczyn.app.auth.presentation.manager.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.auth.domain.model.UserType
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_edit_user

@Composable
fun UserCard(
    user: User,
    onTypeChanged: (UserType) -> Unit,
) {
    var editDialogVisible by remember { mutableStateOf(false) }

    Card(shape = RoundedCornerShape(12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                WolczynText(
                    text = stringResource(user.userType.stringRes),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.outline,
                    ),
                )
                WolczynText(
                    text = "${user.firstName} ${user.lastName}",
                    textStyle = MaterialTheme.typography.titleLarge,
                )
                WolczynText(
                    text = user.email,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.outline,
                    ),
                )
            }
            IconButton(onClick = { editDialogVisible = true }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    tint = wolczynColors.primary,
                    contentDescription = stringResource(Res.string.cd_edit_user),
                )
            }
        }
    }

    EditUserDialog(
        isVisible = editDialogVisible,
        user = user,
        onSave = {
            editDialogVisible = false
            onTypeChanged(it)
        },
        onCancel = { editDialogVisible = false },
    )
}