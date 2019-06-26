package pl.kapucyni.wolczyn.app.view.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_drawer.*
import pl.kapucyni.wolczyn.app.utils.PreferencesManager
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.utils.GlideApp
import pl.kapucyni.wolczyn.app.utils.checkNetworkConnection
import pl.kapucyni.wolczyn.app.utils.getAttributeDrawable
import pl.kapucyni.wolczyn.app.view.fragments.*
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mViewModel: MainViewModel
    private var mCurrentFragmentId: Int? = null
    private var mBackPressed = 0L
    private var mIsTransactionSafe = false
    private var mIsTransactionPending = false
    private var mIsFetched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        if (PreferencesManager.getNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            window.decorView.systemUiVisibility = 0
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (!PreferencesManager.getNightMode() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        mViewModel = ViewModelProviders.of(this@MainActivity).get(MainViewModel::class.java)

        val toggle = ActionBarDrawerToggle(
            this@MainActivity, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        onNavigationItemSelected(savedInstanceState?.let { navView.menu.findItem(it.getInt("selectedItem")) }
            ?: navView.menu.getItem(0))

        mViewModel.userLiveData.observe(this@MainActivity, Observer { user ->
            if (user != null && headerDrawerImage != null) {
                GlideApp.with(this@MainActivity)
                    .load(user.photo_url)
                    .circleCrop()
                    .placeholder(getAttributeDrawable(R.attr.logoMenu))
                    .into(headerDrawerImage)
            } else {
                if (mIsFetched) {
                    Log.d("Bearer Token", "Token expired")
                    signOutUser()
                }
            }
        })
    }

    fun addViewToAppBar(view: View) = appBarLayout.addView(view)

    fun removeViewFromAppBar(view: View) = appBarLayout.removeView(view)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navView.checkedItem?.let { outState.putInt("selectedItem", it.itemId) }
    }

    override fun onPause() {
        super.onPause()
        mIsTransactionSafe = false
    }

    override fun onResume() {
        super.onResume()
        if (!PreferencesManager.getBearerToken().isNullOrEmpty()) {
            if (checkNetworkConnection()) {
                mViewModel.fetchUser()
                mIsFetched = true
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        mIsTransactionSafe = true
        if (mIsTransactionPending) goToSelectedFragment(mCurrentFragmentId!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            if (!data!!.getBooleanExtra("loginSuccess", false) && data.getIntExtra("fragment", 0) == R.id.nav_group)
                goBackToSchedule()
            else
                goToSelectedFragment(data.getIntExtra("fragment", 0))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            mCurrentFragmentId == R.id.nav_songbook ->
                if ((supportFragmentManager.fragments.first { it.javaClass == SongBookFragment::class.java } as SongBookFragment).onBackPressed()) doubleBackPressToExit()
            mCurrentFragmentId == R.id.nav_guests ->
                if ((supportFragmentManager.fragments.first { it.javaClass == ViewPagerFragment::class.java } as ViewPagerFragment).onBackPressed()) doubleBackPressToExit()
            mCurrentFragmentId == R.id.nav_departures ->
                if ((supportFragmentManager.fragments.first { it.javaClass == DepartureListFragment::class.java } as DepartureListFragment).onBackPressed()) doubleBackPressToExit()
            mCurrentFragmentId!! < 0 -> onNavigationItemSelected(navView.menu.getItem(-1 * mCurrentFragmentId!! - 1))
            else -> doubleBackPressToExit()
        }
    }

    private fun doubleBackPressToExit() {
        if (mBackPressed + 2000 > System.currentTimeMillis()) super.onBackPressed()
        else Toast.makeText(baseContext, getString(R.string.press_to_exit), Toast.LENGTH_SHORT).show()
        mBackPressed = System.currentTimeMillis()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when {
            PreferencesManager.getBearerToken().isNullOrEmpty() -> {
                menu?.findItem(R.id.action_sign_in)?.isVisible = true
                menu?.findItem(R.id.action_sign_out)?.isVisible = false
            }
            else -> {
                menu?.findItem(R.id.action_sign_in)?.isVisible = false
                menu?.findItem(R.id.action_sign_out)?.isVisible = true
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sign_in -> {
                openLoginActivity()
                true
            }
            R.id.action_sign_out -> {
                signOutUser()
                true
            }
            R.id.action_settings -> {
                toolbar.title = getString(R.string.preferences)
                supportFragmentManager.beginTransaction().replace(R.id.container, PreferenceFragment()).commit()
                mCurrentFragmentId = -1 * navView.menu.children.indexOfFirst { it.isChecked } - 1
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        mCurrentFragmentId = item.itemId
        navView.setCheckedItem(mCurrentFragmentId!!)
        val fragment = when (item.itemId) {
            R.id.nav_schedule -> {
                toolbar.title = getString(R.string.menu_schedule)
                ScheduleFragment()
            }
            R.id.nav_weather -> {
                toolbar.title = getString(R.string.menu_weather)
                WeatherFragment()
            }
            R.id.nav_anthem -> {
                toolbar.title = getString(R.string.menu_anthem)
                AnthemFragment()
            }
            R.id.nav_songbook -> {
                toolbar.title = getString(R.string.menu_songbook)
                SongBookFragment()
            }
            R.id.nav_group -> {
                if (PreferencesManager.getBearerToken().isNullOrEmpty()) {
                    openLoginActivity()
                    null
                } else {
                    toolbar.title = getString(R.string.menu_group)
                    GroupFragment()
                }
            }
            R.id.nav_guests -> {
                toolbar.title = getString(R.string.menu_guests)
                ViewPagerFragment.newInstance("guests")
            }
            R.id.nav_signings -> {
                toolbar.title = getString(R.string.menu_signings)
                SigningsFragment()
            }
            R.id.nav_departures -> {
                toolbar.title = getString(R.string.menu_departures)
                DepartureListFragment()
            }
            R.id.nav_breviary -> {
                toolbar.title = getString(R.string.menu_breviary)
                ViewPagerFragment.newInstance("breviary")
            }
//            R.id.nav_map -> {
//                toolbar.title = getString(R.string.menu_map)
//                MapFragment()
//            }
//            R.id.nav_showers -> {
//                toolbar.title = getString(R.string.menu_showers)
//            }
            else -> {
                toolbar.title = getString(R.string.menu_schedule)
                ScheduleFragment()
            }
        }

        fragment?.let {
            if (mIsTransactionSafe) supportFragmentManager.beginTransaction().replace(R.id.container, it).commit()
            else mIsTransactionPending = true
        }
        return true
    }

    private fun signOutUser() {
        PreferencesManager.setBearerToken("")
        headerDrawerImage?.setImageDrawable(getAttributeDrawable(R.attr.logoMenu))
        invalidateOptionsMenu()
        if (mCurrentFragmentId == R.id.nav_group) goBackToSchedule()
    }

    private fun openLoginActivity() {
        val intent =
            Intent(this@MainActivity, LoginActivity::class.java).putExtra("fragment", mCurrentFragmentId)
        startActivityForResult(intent, 12)
    }

    fun goBackToSchedule() = onNavigationItemSelected(navView.menu.getItem(0))

    private fun goToSelectedFragment(fragmentId: Int): Boolean =
        onNavigationItemSelected(navView.menu.findItem(fragmentId))
}
