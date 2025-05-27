package pl.kapucyni.wolczyn.app.meetings.presentation.signings.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.composables.WolczynText
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.close
import smwolczyn.composeapp.generated.resources.signings_qr_code_msg

private const val BASIC_URL = "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data="

@Composable
fun SigningsQrCodeDialog(
    email: String?,
    onCancel: () -> Unit,
) {
    email?.let {
        AlertDialog(
            title = {
                WolczynText(
                    text = stringResource(Res.string.signings_qr_code_msg),
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            text = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                ) {
                    CircularProgressIndicator()
                    AsyncImage(
                        model = BASIC_URL + email,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            },
            onDismissRequest = onCancel,
            confirmButton = {
                TextButton(onClick = onCancel) {
                    WolczynText(stringResource(Res.string.close))
                }
            },
        )
    }
}