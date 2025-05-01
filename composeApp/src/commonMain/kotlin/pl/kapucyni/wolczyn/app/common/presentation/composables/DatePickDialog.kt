package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.save

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickDialog(
    isVisible: Boolean,
    dateMillis: Long?,
    onDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit,
) {
    if (isVisible) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                        datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    },
                ) {
                    Text(stringResource(Res.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(Res.string.cancel))
                }
            },
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            DatePicker(
                title = null,
                state = datePickerState,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}