package pl.kapucyni.wolczyn.app.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import pl.kapucyni.wolczyn.app.core.data.sources.FirestoreHomeSource
import pl.kapucyni.wolczyn.app.core.data.sources.WolczynApi
import pl.kapucyni.wolczyn.app.core.domain.model.AppState
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynGroup
import pl.kapucyni.wolczyn.app.core.domain.model.WolczynUser
import pl.kapucyni.wolczyn.app.core.domain.repository.CoreRepository

class CoreRepositoryImpl(
    private val firestoreSource: FirestoreHomeSource,
    private val wolczynApi: WolczynApi,
    private val dataStore: DataStore<Preferences>,
) : CoreRepository {

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = true
    }
    private val userToken = dataStore.data.map { it[USER_TOKEN_KEY] }

    override fun getAppState(): Flow<AppState> =
        combine(
            firestoreSource.getAppConfiguration(),
            firestoreSource.getHomeNotifications(),
        ) { configuration, notifications ->
            AppState(configuration, notifications)
        }

    override suspend fun signIn(login: String, password: String): WolczynUser? =
        wolczynApi.signIn(login, password)
            .saveTokenAndGetUserInfo()

    override suspend fun signInWithGoogle(email: String, identifier: String): WolczynUser? =
        wolczynApi.signInWithGoogle(email, identifier)
            .saveTokenAndGetUserInfo()

    override suspend fun signOut() = saveUserToken("")

    override suspend fun getUserInfo(): WolczynUser? =
        getUserToken()?.let { token ->
            wolczynApi.getUserInfo(token).handleResponse()
        }

    override suspend fun getGroupInfo(): WolczynGroup? =
        getUserToken()?.let { token ->
            wolczynApi.getGroupInfo(token).handleResponse()
        }

    private suspend fun getUserToken() = userToken.firstOrNull()

    private suspend fun saveUserToken(token: String) {
        dataStore.edit { it[USER_TOKEN_KEY] = token }
    }

    private suspend fun Result<String>.saveTokenAndGetUserInfo(): WolczynUser? =
        if (isFailure) null
        else getOrNull()?.let { token ->
            saveUserToken(token)
            getUserInfo()
        }

    private suspend inline fun <reified T> Result<HttpResponse>.handleResponse(): T? =
        if (isFailure) null
        else getOrNull()?.saveTokenAndReturnBody()

    private suspend inline fun <reified T> HttpResponse.saveTokenAndReturnBody(): T? {
        if (headers.contains(HttpHeaders.Authorization))
            saveUserToken(headers[HttpHeaders.Authorization] ?: "")
        return json.decodeFromString<T>(bodyAsText())
    }

    companion object {
        private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
    }
}