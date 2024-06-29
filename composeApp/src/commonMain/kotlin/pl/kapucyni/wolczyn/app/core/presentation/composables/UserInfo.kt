package pl.kapucyni.wolczyn.app.core.presentation.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynGroup
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynUser

@Composable
fun UserInfo(
    user: WolczynUser,
    group: WolczynGroup?
) {
    Text("${user.prefix ?: ""} ${user.name ?: ""} ${user.surname ?: ""}")
}