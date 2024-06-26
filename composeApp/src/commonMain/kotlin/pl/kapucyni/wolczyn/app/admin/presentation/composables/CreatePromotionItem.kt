package pl.kapucyni.wolczyn.app.admin.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import org.jetbrains.compose.resources.stringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_save_promotion
import smwolczyn.composeapp.generated.resources.new_promo_name

@Composable
fun CreatePromotionItem(
    newPromotionName: String,
    onChangeName: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = newPromotionName,
        onValueChange = onChangeName,
        label = { Text(stringResource(Res.string.new_promo_name)) },
        trailingIcon = {
            if (newPromotionName.isNotBlank())
                IconButton(onClick = onSave) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = stringResource(Res.string.cd_save_promotion)
                    )
                }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = { if (newPromotionName.isNotBlank()) onSave() }),
        modifier = modifier.fillMaxWidth()
    )
}