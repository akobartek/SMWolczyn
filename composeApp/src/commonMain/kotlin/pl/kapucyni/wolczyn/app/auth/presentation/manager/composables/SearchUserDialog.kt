package pl.kapucyni.wolczyn.app.auth.presentation.manager.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cancel
import smwolczyn.composeapp.generated.resources.cd_clear_field
import smwolczyn.composeapp.generated.resources.cd_search_user
import smwolczyn.composeapp.generated.resources.search

@Composable
fun SearchUserDialog(
    isVisible: Boolean,
    onSearch: (String) -> Unit,
    onCancel: () -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }

    if (isVisible) {
        val (queryRef) = remember { FocusRequester.createRefs() }

        LaunchedEffect(Unit) {
            queryRef.requestFocus()
        }

        AlertDialog(
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { WolczynText(text = stringResource(Res.string.cd_search_user)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onSearch(query) }),
                        trailingIcon = {
                            if (query.isNotBlank())
                                IconButton(onClick = { query = "" }) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = stringResource(Res.string.cd_clear_field),
                                    )
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(queryRef),
                    )
                }
            },
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = { onSearch(query) }) {
                    WolczynText(stringResource(Res.string.search))
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