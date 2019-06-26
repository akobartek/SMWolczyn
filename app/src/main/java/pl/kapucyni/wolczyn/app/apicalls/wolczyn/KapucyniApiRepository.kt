package pl.kapucyni.wolczyn.app.apicalls.wolczyn

import pl.kapucyni.wolczyn.app.BuildConfig
import pl.kapucyni.wolczyn.app.apicalls.BaseRepository
import pl.kapucyni.wolczyn.app.model.Departure
import pl.kapucyni.wolczyn.app.model.User

class KapucyniApiRepository(private val api: KapucyniApi) : BaseRepository() {

    suspend fun getDepartures(): MutableList<Departure>? =
        safeApiCall({ api.getDeparturesAsync().await() }, "Error fetching departures")?.toMutableList()

    suspend fun loginToSystemWithEmail(login: String, password: String): String? =
        safeApiCall(
            { api.loginToSystemWithEmailAsync(login, password, BuildConfig.KAPUCYNI_API_KEY).await() },
            "Sign in error"
        )

    suspend fun loginToSystemWithSocial(email: String, identifier: String, media: String): String? =
        safeApiCall(
            { api.loginToSystemWithSocialAsync(email, identifier, media, BuildConfig.KAPUCYNI_API_KEY).await() },
            "Sign in error"
        )

    suspend fun getUserInfo(): User? = safeApiCall({ api.getUserInfoAsync().await() }, "Error fetching user")
}