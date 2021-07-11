package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.utils.PreferencesManager

class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        preferenceManager.findPreference<SwitchPreference>(getString(R.string.night_mode_key))?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                requireActivity().let {
                    AppCompatDelegate.setDefaultNightMode(
                        if (newValue as Boolean) AppCompatDelegate.MODE_NIGHT_YES
                        else AppCompatDelegate.MODE_NIGHT_NO
                    )
                    PreferencesManager.setNightMode(newValue)
                }
                true
            }
    }
}