package pl.kapucyni.wolczyn.app.admin.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import pl.kapucyni.wolczyn.app.admin.presentation.model.AdminData
import pl.kapucyni.wolczyn.app.common.presentation.composables.FullScreenDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.HeightSpacer

@Composable
fun AdminDataDialog(
    isVisible: Boolean,
    title: String,
    data: List<AdminData>,
    onPromotionAdd: ((String) -> Unit)?,
    onPromotionUpdate: (String, Boolean) -> Unit,
    onPromotionDelete: ((String) -> Unit)?,
    onDismiss: () -> Unit,
) {
    val density = LocalDensity.current
    var newObjectName by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val listModifier = if (isLoading) Modifier.blur(16.dp) else Modifier
    var listHeight by remember { mutableStateOf(0.dp) }

    LaunchedEffect(key1 = data) {
        isLoading = false
        newObjectName = ""
    }

    FullScreenDialog(
        isVisible = isVisible,
        title = title,
        onAction = null,
        onDismiss = onDismiss,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = listModifier
                .fillMaxSize()
                .onGloballyPositioned { coords ->
                    if (coords.isAttached)
                        listHeight = with(density) { coords.size.height.toDp() }
                }
        ) {
            items(items = data, key = { it.id }) { item ->
                AdminDataListItem(
                    data = item,
                    onPromotionActivation = { id, checked ->
                        isLoading = true
                        onPromotionUpdate(id, checked)
                    },
                    onPromotionDelete = onPromotionDelete?.let {
                        {
                            isLoading = true
                            onPromotionDelete(it)
                        }
                    }
                )
            }
            onPromotionAdd?.let {
                item {
                    CreatePromotionItem(
                        newPromotionName = newObjectName,
                        onChangeName = { newObjectName = it },
                        onSave = {
                            isLoading = true
                            onPromotionAdd(newObjectName.trim())
                        },
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
            item {
                HeightSpacer(12.dp)
            }
        }
    }
}