package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import pl.kapucyni.wolczyn.app.common.presentation.composables.FullScreenDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.core.presentation.model.AuthDialogState
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.sign_out

@Composable
fun AuthDialog(
    state: AuthDialogState,
    onSignIn: (String, String) -> Unit,
    onSignOut: () -> Unit,
    onDismiss: () -> Unit,
    loadGroupInfo: () -> Unit,
) {
    LaunchedEffect(state) {
        if (state.isDialogVisible && state.user != null && state.user.type == 2)
            loadGroupInfo()
    }

    FullScreenDialog(
        isVisible = state.isDialogVisible,
        title = "",
        actionTitle = Res.string.sign_out,
        onAction = if (state.user != null) onSignOut else null,
        onDismiss = onDismiss,
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            if (state.isLoading) LoadingBox()

            when {
                state.user == null -> AuthSignIn(
                    isVisible = !state.isLoading,
                    signInError = state.signInError,
                    onSignIn = onSignIn
                )

                else -> UserInfo(user = state.user, group = state.group)
            }
        }
    }
}