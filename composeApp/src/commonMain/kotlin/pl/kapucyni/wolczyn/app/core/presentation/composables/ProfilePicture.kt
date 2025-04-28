package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_account_details
import smwolczyn.composeapp.generated.resources.cd_sign_in

@Composable
fun ProfilePicture(user: User?) {
    val placeholder = rememberVectorPainter(
        if (user != null) Icons.Filled.AccountCircle
        else Icons.Outlined.AccountCircle,
    )
    val descriptionRes = if (user != null) Res.string.cd_account_details else Res.string.cd_sign_in

    if (user?.photoUrl.isNullOrBlank())
        Icon(
            painter = placeholder,
            tint = wolczynColors.primary,
            contentDescription = stringResource(descriptionRes),
        )
    else
        AsyncImage(
            model = user?.photoUrl,
            contentDescription = stringResource(descriptionRes),
            contentScale = ContentScale.Crop,
            error = placeholder,
            placeholder = placeholder,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
        )
}