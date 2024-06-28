package pl.kapucyni.wolczyn.app.core.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.kapucyni.wolczyn.app.core.domain.model.AppState
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynGroup
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynUser

interface CoreRepository {
    fun getAppState(): Flow<AppState>
    suspend fun login(login: String, password: String): WolczynUser?
    suspend fun loginWithGoogle(email: String, identifier: String): WolczynUser?
    suspend fun getUserInfo(): WolczynUser?
    suspend fun getGroupInfo(): WolczynGroup?
}