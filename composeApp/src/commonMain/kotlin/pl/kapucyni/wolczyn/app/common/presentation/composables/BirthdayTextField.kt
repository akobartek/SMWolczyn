package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.utils.getFormattedDate
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.birthday_error
import smwolczyn.composeapp.generated.resources.user_birthday

@Composable
fun BirthdayTextField(
    modifier: Modifier,
    value: Long?,
    onDateSelected: (Long) -> Unit,
    error: Boolean,
) {
    val focusManager = LocalFocusManager.current
    var dateDialogVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = value?.getFormattedDate().orEmpty(),
        onValueChange = {},
        label = { WolczynText(text = stringResource(Res.string.user_birthday)) },
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
        isError = error,
        supportingText = if (error) {
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
            .then(modifier),
    )

    DatePickDialog(
        isVisible = dateDialogVisible,
        dateMillis = value,
        onDismiss = {
            focusManager.clearFocus()
            dateDialogVisible = false
        },
        onDateSelected = onDateSelected,
    )
}