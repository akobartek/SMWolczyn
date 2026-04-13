package pl.kapucyni.wolczyn.app.core.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private val profileVerifiedKey = booleanPreferencesKey("is_profile_verified")

    val isProfileVerified: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[profileVerifiedKey] ?: false
    }

    suspend fun setProfileVerified(isVerified: Boolean) {
        dataStore.edit { preferences ->
            preferences[profileVerifiedKey] = isVerified
        }
    }
}