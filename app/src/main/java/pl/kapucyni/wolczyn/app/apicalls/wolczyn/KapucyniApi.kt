package pl.kapucyni.wolczyn.app.apicalls.wolczyn

import pl.kapucyni.wolczyn.app.model.Group
import pl.kapucyni.wolczyn.app.model.User
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface KapucyniApi {

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginToSystemWithEmailAsync(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("app_id") app_id: String
    ): Response<String>

    @FormUrlEncoded
    @POST("auth/social")
    suspend fun loginToSystemWithSocialAsync(
        @Field("email") email: String,
        @Field("identifier") identifier: String,
        @Field("media") media: String,
        @Field("app_id") appId: String
    ): Response<String>

    @POST("wolczyn/ja")
    suspend fun getUserInfoAsync(): Response<User>

    @POST("wolczyn/grupa")
    suspend fun getGroupInfoAsync(): Response<Group>
}