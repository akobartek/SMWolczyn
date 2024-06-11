package pl.kapucyni.wolczyn.app.shop.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import pl.kapucyni.wolczyn.app.shop.domain.model.ShopProduct
import pl.kapucyni.wolczyn.app.theme.appColorSecondary

@Composable
fun ProductListItem(
    product: ShopProduct,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // TODO() -> replace with image
        Spacer(
            modifier = Modifier
                .background(appColorSecondary)
                .fillMaxWidth()
                .height(220.dp)
        )
        WolczynText(
            text = product.name,
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 32.sp,
                color = MaterialTheme.colorScheme.onSurface,
            ),
            isOneLiner = true,
        )
    }
}