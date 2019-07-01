package pl.kapucyni.wolczyn.app.apicalls.wolczyn

import pl.kapucyni.wolczyn.app.BuildConfig
import pl.kapucyni.wolczyn.app.apicalls.RetrofitClient
import pl.kapucyni.wolczyn.app.model.User
import retrofit2.Response

class KapucyniApiRepository(private val api: KapucyniApi) {

    suspend fun getDepartures() = RetrofitClient.kapucyniApi.getDeparturesAsync()

    suspend fun loginToSystemWithEmail(login: String, password: String): String? =
        api.loginToSystemWithEmailAsync(login, password, BuildConfig.KAPUCYNI_API_KEY)

    suspend fun loginToSystemWithSocial(email: String, identifier: String, media: String): String? =
        api.loginToSystemWithSocialAsync(email, identifier, media, BuildConfig.KAPUCYNI_API_KEY)

    suspend fun getUserInfo(): Response<User> = api.getUserInfoAsync()
}