package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_clear_field
import smwolczyn.composeapp.generated.resources.ic_close
import smwolczyn.composeapp.generated.resources.search

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { WolczynText(text = stringResource(Res.string.search)) },
        singleLine = true,
        trailingIcon = {
            if (value.isNotBlank())
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_close),
                        contentDescription = stringResource(Res.string.cd_clear_field),
                    )
                }
        },
        modifier = Modifier.fillMaxWidth().then(modifier),
    )
}