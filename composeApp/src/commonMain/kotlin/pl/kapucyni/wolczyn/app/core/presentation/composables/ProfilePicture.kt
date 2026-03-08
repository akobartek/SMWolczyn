package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import pl.kapucyni.wolczyn.app.auth.domain.model.User
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.cd_account_details
import smwolczyn.composeapp.generated.resources.cd_sign_in
import smwolczyn.composeapp.generated.resources.ic_account_circle
import smwolczyn.composeapp.generated.resources.ic_account_circle_filled

@Composable
fun ProfilePicture(user: User?) {
    val placeholder = rememberVectorPainter(
        vectorResource(
            if (user != null) Res.drawable.ic_account_circle_filled
            else Res.drawable.ic_account_circle
        )
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
            model = user.photoUrl,
            contentDescription = stringResource(descriptionRes),
            contentScale = ContentScale.Crop,
            error = placeholder,
            placeholder = placeholder,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
        )
}