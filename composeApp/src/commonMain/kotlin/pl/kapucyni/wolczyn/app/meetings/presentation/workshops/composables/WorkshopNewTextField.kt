package pl.kapucyni.wolczyn.app.meetings.presentation.workshops.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.save
import smwolczyn.composeapp.generated.resources.workshop_new_name

@Composable
fun WorkshopNewTextField(
    onSave: (String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val saveWorkshop = {
        focusManager.clearFocus()
        if (name.length > 3)
            onSave(name)
    }

    OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { WolczynText(text = stringResource(Res.string.workshop_new_name)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Sentences,
        ),
        trailingIcon = if (name.length > 3) {
            {
                IconButton(onClick = saveWorkshop) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(Res.string.save),
                    )
                }
            }
        } else null,
        keyboardActions = KeyboardActions(onDone = { saveWorkshop() }),
        singleLine = true,
        modifier = Modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth(),
    )
}