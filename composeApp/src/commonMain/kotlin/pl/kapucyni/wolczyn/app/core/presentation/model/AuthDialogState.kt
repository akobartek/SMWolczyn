package pl.kapucyni.wolczyn.app.core.presentation.model

import pl.kapucyni.wolczyn.app.core.domain.model.WolczynGroup
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynUser

data class AuthDialogState(
    val isDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val signInError: Boolean = false,
    val user: WolczynUser? = null,
    val group: WolczynGroup? = null,
) {
    fun showDialog() = copy(isDialogVisible = true)

    fun hideDialog() = copy(isDialogVisible = false)

    fun loading() = copy(isLoading = true)

    fun signedIn(user: WolczynUser) =
        copy(isDialogVisible = false, isLoading = false, signInError = false, user = user)

    fun signedOut() =
        copy(isDialogVisible = false, isLoading = false, signInError = false, user = null)

    fun loginError() = copy(signInError = true, isLoading = false)

    fun groupLoaded(group: WolczynGroup?) = copy(group = group, isLoading = false)
}
