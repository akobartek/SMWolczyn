package pl.kapucyni.wolczyn.app.meetings.presentation.participants.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.meetings.domain.model.Workshop
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.ic_construction
import smwolczyn.composeapp.generated.resources.save
import smwolczyn.composeapp.generated.resources.workshops

@Composable
fun WorkshopChangeDialog(
    isVisible: Boolean,
    currentWorkshop: String,
    workshops: List<Pair<Workshop, Int>>,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    if (isVisible) {
        var selectedOption by remember { mutableStateOf(currentWorkshop) }

        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_construction),
                    contentDescription = null,
                )
            },
            title = {
                WolczynText(
                    text = stringResource(Res.string.workshops),
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Center,
                    ),
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .selectableGroup()
                        .verticalScroll(rememberScrollState()),
                ) {
                    if (workshops.isEmpty())
                        CircularProgressIndicator()

                    workshops.forEach { (workshop, count) ->
                        val selected = workshop.name == selectedOption
                        Row(
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .selectable(
                                    selected = selected,
                                    onClick = { selectedOption = workshop.name },
                                    role = Role.RadioButton,
                                )
                                .background(
                                    color =
                                        if (selected) MaterialTheme.colorScheme.secondaryContainer
                                        else MaterialTheme.colorScheme.background,
                                )
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            WolczynText(
                                text = "${workshop.name} ($count)",
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color =
                                        if (selected) MaterialTheme.colorScheme.onSecondaryContainer
                                        else MaterialTheme.colorScheme.onBackground,
                                ),
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = currentWorkshop != selectedOption && selectedOption.isNotBlank(),
                    onClick = { onSave(selectedOption) },
                ) {
                    WolczynText(text = stringResource(Res.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    WolczynText(text = stringResource(Res.string.cancel))
                }
            },
        )
    }
}

@Preview
@Composable
private fun WorkshopChangeDialogPreview() {
    WorkshopChangeDialog(
        isVisible = true,
        currentWorkshop = "Piłkarskie",
        workshops = listOf(
            Workshop(name = "Taneczne") to 7,
            Workshop(name = "Wokalne") to 7,
            Workshop(name = "Piłkarskie") to 7,
            Workshop(name = "Modlitewne") to 7,
        ),
        onSave = {},
        onDismiss = {},
    )
}