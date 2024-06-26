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
import pl.kapucyni.wolczyn.app.common.data.model.FirestorePromotion
import pl.kapucyni.wolczyn.app.common.presentation.composables.FullScreenDialog

@Composable
fun PromotionsDialog(
    isVisible: Boolean,
    title: String,
    promotions: List<FirestorePromotion>,
    onPromotionAdd: (String) -> Unit,
    onPromotionUpdate: (FirestorePromotion) -> Unit,
    onPromotionDelete: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val density = LocalDensity.current
    var newPromotionName by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val listModifier = if (isLoading) Modifier.blur(16.dp) else Modifier
    var listHeight by remember { mutableStateOf(0.dp) }

    LaunchedEffect(key1 = promotions) {
        isLoading = false
        newPromotionName = ""
    }

    FullScreenDialog(
        isVisible = isVisible,
        title = title,
        onSave = null,
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
            items(items = promotions, key = { it.id }) { promotion ->
                PromotionListItem(
                    promotion = promotion,
                    onPromotionActivation = {
                        isLoading = true
                        onPromotionUpdate(it)
                    },
                    onPromotionDelete = {
                        isLoading = true
                        onPromotionDelete(it)
                    },
                )
            }
            item {
                CreatePromotionItem(
                    newPromotionName = newPromotionName,
                    onChangeName = { newPromotionName = it },
                    onSave = {
                        isLoading = true
                        onPromotionAdd(newPromotionName.trim())
                    },
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}