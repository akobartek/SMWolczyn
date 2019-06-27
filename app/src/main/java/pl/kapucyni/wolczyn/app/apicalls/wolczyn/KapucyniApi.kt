package pl.kapucyni.wolczyn.app.apicalls.wolczyn

import kotlinx.coroutines.Deferred
import pl.kapucyni.wolczyn.app.model.Departure
import pl.kapucyni.wolczyn.app.model.User
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface KapucyniApi {

    @GET("wolczyn/wyjazdy")
    fun getDeparturesAsync(): Deferred<Response<List<Departure>>>

    @FormUrlEncoded
    @POST("auth/login")
    fun loginToSystemWithEmailAsync(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("app_id") app_id: String
    ): Deferred<Response<String>>

    @FormUrlEncoded
    @POST("auth/social")
    fun loginToSystemWithSocialAsync(
        @Field("email") email: String,
        @Field("identifier") identifier: String,
        @Field("media") media: String,
        @Field("app_id") app_id: String
    ): Deferred<Response<String>>

    @POST("wolczyn/ja")
    fun getUserInfoAsync(): Deferred<Response<User>>
}