package pl.kapucyni.wolczyn.app.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.data.sources.WolczynApi
import pl.kapucyni.wolczyn.app.core.domain.model.AppState
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynGroup
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynUser
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository

class CoreRepositoryImpl(
    private val firestoreSource: FirestoreHomeSource,
    private val wolczynApi: WolczynApi,
) : CoreRepository {

    override fun getAppState(): Flow<AppState> =
        combine(
            firestoreSource.getAppConfiguration(),
            firestoreSource.getHomeNotifications(),
        ) { configuration, notifications ->
            AppState(configuration, notifications)
        }

    override suspend fun login(login: String, password: String): WolczynUser? {
        val result = wolczynApi.login(login, password)
        if (result.isFailure) return null



        return getUserInfo()
    }

    override suspend fun loginWithGoogle(email: String, identifier: String): WolczynUser? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfo(): WolczynUser? {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupInfo(): WolczynGroup? {
        TODO("Not yet implemented")
    }
}