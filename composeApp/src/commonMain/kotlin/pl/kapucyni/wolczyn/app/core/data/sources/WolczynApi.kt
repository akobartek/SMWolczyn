package pl.kapucyni.wolczyn.app.core.data.sources

import SMWolczyn.composeApp.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters

class WolczynApi(private val httpClient: HttpClient) {

    companion object {
        private const val PARAMETER_KEY_APP_ID = "app_id"
        private const val PARAMETER_KEY_LOGIN = "login"
        private const val PARAMETER_KEY_PASSWORD = "password"
        private const val PARAMETER_KEY_EMAIL = "password"
        private const val PARAMETER_KEY_IDENTIFIER = "identifier"
        private const val PARAMETER_KEY_MEDIA = "media"
        private const val MEDIA_TYPE_GOOGLE = "google"
    }

    suspend fun login(login: String, password: String): Result<String> = runCatching {
        httpClient.submitForm(
            url = "auth/login",
            formParameters = parameters {
                append(PARAMETER_KEY_LOGIN, login)
                append(PARAMETER_KEY_PASSWORD, password)
                append(PARAMETER_KEY_APP_ID, BuildConfig.KAPUCYNI_API_KEY)
            }
        ).body<String>().correctResponse()
    }

    suspend fun loginWithGoogle(email: String, identifier: String): Result<String> = runCatching {
        httpClient.submitForm(
            url = "auth/social",
            formParameters = parameters {
                append(PARAMETER_KEY_EMAIL, email)
                append(PARAMETER_KEY_IDENTIFIER, identifier)
                append(PARAMETER_KEY_MEDIA, MEDIA_TYPE_GOOGLE)
                append(PARAMETER_KEY_APP_ID, BuildConfig.KAPUCYNI_API_KEY)
            }
        ).body<String>().correctResponse()
    }

    suspend fun getUserInfo(token: String): Result<HttpResponse> = runCatching {
        httpClient.post("wolczyn/ja") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    suspend fun getGroupInfo(token: String): Result<HttpResponse> = runCatching {
        httpClient.post("wolczyn/grupa") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }

    private fun String.correctResponse() = this.removeSurrounding("\"")
}