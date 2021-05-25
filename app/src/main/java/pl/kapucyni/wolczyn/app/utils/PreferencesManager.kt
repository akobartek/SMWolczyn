package pl.kapucyni.wolczyn.app.utils

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import pl.kapucyni.wolczyn.app.SMWolczynApp

object PreferencesManager {

    private const val BEARER_TOKEN = "bearer_token"
    private const val NIGHT_MODE = "night_mode"
    private val sharedPref: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(SMWolczynApp.instance)

    fun setBearerToken(bearerToken: String) {
        sharedPref.edit()
            .putString(BEARER_TOKEN, bearerToken)
            .apply()
    }

    fun getBearerToken(): String? = sharedPref.getString(BEARER_TOKEN, "")

    fun getNightMode() = sharedPref.getBoolean(NIGHT_MODE, false)

    fun setNightMode(newValue: Boolean) {
        sharedPref.edit()
            .putBoolean(NIGHT_MODE, newValue)
            .apply()
    }
}