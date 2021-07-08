package pl.kapucyni.wolczyn.app.view.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import pl.kapucyni.wolczyn.app.BuildConfig
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.ActivityMainBinding
import pl.kapucyni.wolczyn.app.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.utils.*
import pl.kapucyni.wolczyn.app.view.fragments.*
import pl.kapucyni.wolczyn.app.viewmodels.MainViewModel
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel
    private var mCurrentFragmentId: Int? = null
    private var mBackPressed = 0L
    private var mIsTransactionSafe = false
    private var mIsTransactionPending = false
    private var mIsFetched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        if (PreferencesManager.getNightMode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainAppBar.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (!PreferencesManager.getNightMode()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        mViewModel = ViewModelProvider(this@MainActivity).get(MainViewModel::class.java)

        val toggle = ActionBarDrawerToggle(
            this@MainActivity, binding.drawerLayout, binding.mainAppBar.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)
        onNavigationItemSelected(savedInstanceState?.let {
            binding.navView.menu.findItem(it.getInt("selectedItem", 0))
        } ?: binding.navView.menu.getItem(0))

        mViewModel.currentUser.observe(this@MainActivity, { user ->
            if (user != null && binding.navView.getHeaderView(0) != null) {
                GlideApp.with(this@MainActivity)
                    .load(user.photo_url)
                    .circleCrop()
                    .placeholder(getAttributeDrawable(R.attr.logoMenu))
                    .into(binding.navView.getHeaderView(0) as ImageView)
            } else {
                if (mIsFetched) {
                    if (BuildConfig.DEBUG) Log.d("Bearer Token", "Token expired")
                    signOutUser()
                }
            }
        })

        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) > 12 && !PreferencesManager.wasMFTauAdShowed()) {
            openMFTauDialog()
            PreferencesManager.markAdAsShowed()
        }
    }

    fun addViewToAppBar(view: View) = binding.mainAppBar.appBarLayout.addView(view)
    fun addViewToToolbar(view: View) = binding.mainAppBar.toolbar.addView(view)
    fun removeViewFromAppBar(view: View) = binding.mainAppBar.appBarLayout.removeView(view)
    fun removeViewFromToolbar(view: View) = binding.mainAppBar.toolbar.removeView(view)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.navView.checkedItem?.let { outState.putInt("selectedItem", it.itemId) }
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

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) ->
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            mCurrentFragmentId == R.id.nav_schedule ->
                if ((supportFragmentManager.fragments.first { it.javaClass == ScheduleFragment::class.java } as ScheduleFragment).onBackPressed()) doubleBackPressToExit()
            mCurrentFragmentId == R.id.nav_songbook ->
                if ((supportFragmentManager.fragments.first { it.javaClass == SongBookFragment::class.java } as SongBookFragment).onBackPressed()) doubleBackPressToExit()
            mCurrentFragmentId == R.id.nav_guests ->
                if ((supportFragmentManager.fragments.first { it.javaClass == ViewPagerFragment::class.java } as ViewPagerFragment).onBackPressed()) doubleBackPressToExit()
            mCurrentFragmentId == R.id.nav_departures ->
                if ((supportFragmentManager.fragments.first { it.javaClass == DepartureListFragment::class.java } as DepartureListFragment).onBackPressed()) doubleBackPressToExit()
            mCurrentFragmentId!! < 0 ->
                onNavigationItemSelected(binding.navView.menu.getItem(-1 * mCurrentFragmentId!! - 1))
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
                menu?.findItem(R.id.action_sign_in)?.isVisible = false
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
                binding.mainAppBar.toolbar.title = getString(R.string.preferences)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PreferenceFragment()).commit()
                mCurrentFragmentId =
                    -1 * binding.navView.menu.children.indexOfFirst { it.isChecked } - 1
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        mCurrentFragmentId = item.itemId
        binding.navView.setCheckedItem(mCurrentFragmentId!!)
        val fragment = when (item.itemId) {
            R.id.nav_home -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_home)
                HomeFragment()
            }
            R.id.nav_archive -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_archive)
                ArchiveFragment()
            }
            R.id.nav_schedule -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_schedule)
                ScheduleFragment()
            }
            R.id.nav_weather -> {
                binding.mainAppBar.toolbar.title = getString(R.string.weather_title, "Wołczyn")
                WeatherFragment()
            }
            R.id.nav_anthem -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_anthem)
                AnthemFragment()
            }
            R.id.nav_songbook -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_songbook)
                SongBookFragment()
            }
            R.id.nav_breviary -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_breviary)
                ViewPagerFragment.newInstance("breviary")
            }
            R.id.nav_group -> {
                if (PreferencesManager.getBearerToken().isNullOrEmpty()) {
                    openLoginActivity()
                    null
                } else {
                    binding.mainAppBar.toolbar.title = getString(R.string.menu_group)
                    GroupFragment()
                }
            }
            R.id.nav_guests -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_guests)
                ViewPagerFragment.newInstance("guests")
            }
            R.id.nav_signings -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_signings)
                SigningsFragment()
            }
            R.id.nav_departures -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_departures)
                DepartureListFragment()
            }
            R.id.nav_map -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_map)
                MapFragment()
            }
            else -> {
                binding.mainAppBar.toolbar.title = getString(R.string.menu_schedule)
                ScheduleFragment()
            }
        }

        fragment?.let {
            if (mIsTransactionSafe) supportFragmentManager.beginTransaction()
                .replace(R.id.container, it).commit()
            else mIsTransactionPending = true
        }
        return true
    }

    private fun signOutUser() {
        PreferencesManager.setBearerToken("")
        (binding.navView.getHeaderView(0) as ImageView?)?.setImageDrawable(getAttributeDrawable(R.attr.logoMenu))
        invalidateOptionsMenu()
        if (mCurrentFragmentId == R.id.nav_group) goBackToHome()
    }

    private val openLoginActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (!it.data!!.getBooleanExtra("loginSuccess", false)
                    && it.data!!.getIntExtra("fragment", 0) == R.id.nav_group
                ) goBackToHome()
                else goToSelectedFragment(it.data!!.getIntExtra("fragment", 0))
            }
        }

    private fun openLoginActivity() {
        openLoginActivity.launch(
            Intent(this@MainActivity, LoginActivity::class.java)
                .putExtra("fragment", mCurrentFragmentId)
        )
    }

    private val openArchiveDetails =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    fun openArchiveDetailsActivity(meeting: ArchiveMeeting) {
        val intent = Intent(this@MainActivity, ArchiveMeetingDetailsActivity::class.java)
        intent.putExtra("title", meeting.name.split(" - ")[1])
        intent.putParcelableArrayListExtra("records", meeting.records)
        openArchiveDetails.launch(intent)
    }

    fun changeToolbarTitle(newTitle: String) {
        binding.mainAppBar.toolbar.title = newTitle
    }

    fun goBackToHome() = onNavigationItemSelected(binding.navView.menu.getItem(0))

    fun goToSelectedFragment(fragmentId: Int): Boolean =
        onNavigationItemSelected(binding.navView.menu.findItem(fragmentId))
}
