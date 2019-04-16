package pl.kapucyni.wolczyn.app.view.activities

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.view.fragments.MainFragment
import pl.kapucyni.wolczyn.app.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mMainViewModel: MainViewModel
    private var isNightMode = false
    private var currentFragmentId = 0

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        if (PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                .getBoolean(getString(R.string.night_mode_key), false)) {
//            setTheme(R.style.AppTheme_Dark)
            window.decorView.systemUiVisibility = 0
            isNightMode = true
        } else {
            setTheme(R.style.AppTheme_Light)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (!isNightMode && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        val navController = (navHostFragment as NavHostFragment? ?: return).navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        mMainViewModel = ViewModelProviders.of(this@MainActivity).get(MainViewModel::class.java)
        mMainViewModel.isNightMode = isNightMode

        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentFragmentId = destination.id

            when (currentFragmentId) {
                R.id.mainFragment -> supportActionBar?.hide()
                else -> supportActionBar?.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isNightMode != PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                .getBoolean(getString(R.string.night_mode_key), false)) {
            recreate()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.navHostFragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when (currentFragmentId) {
            // First app version - no other fragments
//            R.id.mainFragment -> super.onBackPressed()
            R.id.mainFragment -> (supportFragmentManager.findFragmentById(R.id.navHostFragment)!!
                .childFragmentManager.fragments[0] as MainFragment).onBackPressed()
            else -> onSupportNavigateUp()
        }
    }
}
