package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.runtime.Composable
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
) {
    FullScreenDialog(
        isVisible = state.isDialogVisible,
        title = "",
        actionTitle = Res.string.sign_out,
        onAction = if (state.user != null) onSignOut else null,
        onDismiss = onDismiss,
    ) {
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