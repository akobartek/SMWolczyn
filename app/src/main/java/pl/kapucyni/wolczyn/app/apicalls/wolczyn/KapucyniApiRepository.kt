package pl.kapucyni.wolczyn.app.apicalls.wolczyn

import pl.kapucyni.wolczyn.app.BuildConfig
import pl.kapucyni.wolczyn.app.apicalls.RetrofitClient
import pl.kapucyni.wolczyn.app.model.Group
import pl.kapucyni.wolczyn.app.model.User
import retrofit2.Response

class KapucyniApiRepository(private val api: KapucyniApi) {

    suspend fun getDepartures() = RetrofitClient.kapucyniApi.getDeparturesAsync()

    suspend fun loginToSystemWithEmail(login: String, password: String): String? {
        val response = api.loginToSystemWithEmailAsync(login, password, BuildConfig.KAPUCYNI_API_KEY)
        if (response.isSuccessful) return response.body()
        else throw IllegalStateException(response.errorBody()?.string())
    }

    suspend fun loginToSystemWithSocial(email: String, identifier: String, media: String): String? {
        val response = api.loginToSystemWithSocialAsync(email, identifier, media, BuildConfig.KAPUCYNI_API_KEY)
        if (response.isSuccessful) return response.body()
        else throw IllegalStateException(response.errorBody()?.string())
    }

    suspend fun getUserInfo(): Response<User> = api.getUserInfoAsync()

    suspend fun getGroupInfo(): Response<Group> = api.getGroupInfoAsync()
}