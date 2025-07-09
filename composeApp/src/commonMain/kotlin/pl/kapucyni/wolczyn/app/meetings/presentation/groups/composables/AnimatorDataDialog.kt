package pl.kapucyni.wolczyn.app.meetings.presentation.groups.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.SelectableTextView
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.contact_number
import smwolczyn.composeapp.generated.resources.current_group
import smwolczyn.composeapp.generated.resources.save

@Composable
fun AnimatorDataDialog(
    isVisible: Boolean,
    name: String,
    contact: String,
    currentGroup: Int,
    numberOfGroups: Int,
    onConfirm: (Int, String) -> Unit,
    onDismiss: () -> Unit,
) {
    if (isVisible) {
        var newGroup by rememberSaveable { mutableStateOf(currentGroup) }
        var newContact by rememberSaveable { mutableStateOf(contact) }

        AlertDialog(
            icon = {
                Icon(
                    imageVector = Icons.Filled.SupervisedUserCircle,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            title = {
                WolczynText(
                    text = name,
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Center,
                    ),
                )
            },
            text = {
                Column {
                    SelectableTextView(
                        value = newGroup.toString(),
                        label = Res.string.current_group,
                        items = (1..numberOfGroups).toList().map { it to it.toString() },
                        onItemSelected = { newGroup = it },
                    )

                    OutlinedTextField(
                        value = newContact,
                        onValueChange = { newContact = it },
                        singleLine = true,
                        label = { WolczynText(text = stringResource(Res.string.contact_number)) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    )
                }
            },
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    onDismiss()
                    onConfirm(newGroup, newContact)
                }) {
                    Text(stringResource(Res.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(Res.string.cancel))
                }
            },
            modifier = Modifier.heightIn(max = 560.dp),
        )
    }
}