package pl.kapucyni.wolczyn.app.apicalls

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.kapucyni.wolczyn.app.apicalls.weather.WeatherApi
import pl.kapucyni.wolczyn.app.apicalls.wolczyn.KapucyniApi
import pl.kapucyni.wolczyn.app.utils.PreferencesManager
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    private const val BASE_URL_KAPUCYNI_API = "https://api.kapucyni.pl/"
    private var kapucyniApiClient: Retrofit? = null

    private const val BASE_URL_WEATHER =
        "http://api.openweathermap.org/data/2.5/"
    private var weatherClient: Retrofit? = null

    private fun getKapucyniApiClient(): Retrofit {
        if (kapucyniApiClient === null) {
            kapucyniApiClient = Retrofit.Builder()
                .baseUrl(BASE_URL_KAPUCYNI_API)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(getOkHttpClient())
                .build()
        }
        return kapucyniApiClient!!
    }

    val kapucyniApi: KapucyniApi = getKapucyniApiClient().create(KapucyniApi::class.java)
    val authorizedKapucyniApi: KapucyniApi = getAuthorizationKapucyniApiClient().create(KapucyniApi::class.java)

    private fun getWeatherClient(): Retrofit {
        if (weatherClient === null) {
            weatherClient = Retrofit.Builder()
                .baseUrl(BASE_URL_WEATHER)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(getOkHttpClient())
                .build()
        }
        return weatherClient!!
    }

    val weatherApi: WeatherApi = getWeatherClient().create(WeatherApi::class.java)

    private fun getAuthorizationKapucyniApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_KAPUCYNI_API)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(getAuthorizationOkHttpClient())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request()
                request = request.newBuilder()
                    .build()
                chain.proceed(request)
            }
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }


    private fun getAuthorizationOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request()
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer ${PreferencesManager.getBearerToken()}")
                    .build()

                chain.proceed(request)
            }
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}