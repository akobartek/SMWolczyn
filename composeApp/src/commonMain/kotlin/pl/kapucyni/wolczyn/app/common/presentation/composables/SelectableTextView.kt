package pl.kapucyni.wolczyn.app.common.presentation.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.ic_arrow_drop_down

@Composable
fun <T> SelectableTextView(
    modifier: Modifier = Modifier,
    value: String,
    label: StringResource,
    items: List<Pair<T, String>>,
    onItemSelected: (T) -> Unit,
    leadingIcon: ImageVector? = null,
    error: StringResource? = null,
) {
    val focusManager = LocalFocusManager.current
    var dropDownVisible by rememberSaveable { mutableStateOf(false) }
    val angle by animateFloatAsState(targetValue = if (dropDownVisible) 180f else 0f)

    BoxWithConstraints(
        modifier = Modifier
            .widthIn(max = 420.dp)
            .fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = { WolczynText(stringResource(label)) },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                    )
                }
            },
            trailingIcon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_arrow_drop_down),
                    contentDescription = null,
                    modifier = Modifier.graphicsLayer {
                        rotationZ = angle
                    },
                )
            },
            readOnly = true,
            singleLine = true,
            isError = error != null,
            supportingText = error?.let {
                {
                    WolczynText(text = stringResource(error))
                }
            },
            modifier = Modifier
                .onFocusChanged {
                    if (it.isFocused) dropDownVisible = true
                }
                .fillMaxWidth()
                .then(modifier),
        )

        DropdownMenu(
            expanded = dropDownVisible,
            onDismissRequest = {
                focusManager.clearFocus()
                dropDownVisible = false
            },
            modifier = Modifier.width(maxWidth),
        ) {
            items.forEach { (item, text) ->
                DropdownMenuItem(
                    text = { WolczynText(text = text) },
                    onClick = {
                        focusManager.clearFocus()
                        dropDownVisible = false
                        onItemSelected(item)
                    },
                )
            }
        }
    }
}