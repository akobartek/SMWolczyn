package pl.kapucyni.wolczyn.app.view.fragments


import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.utils.PreferencesManager

class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        preferenceManager.findPreference(getString(R.string.night_mode_key))
            .setOnPreferenceChangeListener { _, newValue ->
                activity?.let {
                    AppCompatDelegate.setDefaultNightMode(
                        if (newValue as Boolean) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                    )
                    PreferencesManager.setNightMode(newValue)
                }
                true
            }
    }
}