package pl.kapucyni.wolczyn.app.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.google.android.material.elevation.SurfaceColors
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

    companion object {
        const val APP_MEETING_MODE_ACTIVATED = false
        private const val BREVIARY_FRAGMENT_ID = 2137
    }

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mViewModel: MainViewModel by viewModels()
    private var mCurrentFragmentId: Int? = null
    private var mBackPressed = 0L
    private var mIsTransactionSafe = false
    private var mIsTransactionPending = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        if (PreferencesManager.getNightMode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.contentMain.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val color = SurfaceColors.SURFACE_5.getColor(this)
        window.statusBarColor = color
        window.navigationBarColor = color

        val toggle = ActionBarDrawerToggle(
            this@MainActivity, binding.drawerLayout, binding.contentMain.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.apply {
            menu.clear()
            inflateMenu(if (APP_MEETING_MODE_ACTIVATED) R.menu.menu_drawer_normal else R.menu.menu_drawer_after)
            setNavigationItemSelectedListener(this@MainActivity)
        }

        onNavigationItemSelected(savedInstanceState?.let {
            binding.navView.menu.findItem(it.getInt("selectedItem", 0))
        } ?: binding.navView.menu.getItem(0))

        mViewModel.isUserFetched.observe(this@MainActivity) { isUserFetched ->
            if (isUserFetched) {
                val user = mViewModel.currentUser.value
                if (user != null && binding.navView.findViewById<ImageView>(R.id.headerDrawerImage) != null) {
                    GlideApp.with(this@MainActivity)
                        .load(user.photo_url)
                        .circleCrop()
                        .placeholder(R.drawable.ic_logo)
                        .into(binding.navView.findViewById(R.id.headerDrawerImage))
                    (binding.navView.findViewById<TextView>(R.id.headerDrawerName)).text = user.name
                } else {
                    if (BuildConfig.DEBUG) Log.d("Bearer Token", "Token expired")
                    Toast.makeText(this, getString(R.string.token_expired), Toast.LENGTH_SHORT)
                        .show()
                    signOutUser()
                }
                mViewModel.isUserFetched.postValue(false)
            }
        }

        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) > 10
            && Calendar.getInstance().get(Calendar.MONTH) > 6
            && !PreferencesManager.wasMFTauAdShowed()
        ) {
            showMFTauDialog()
            PreferencesManager.markAdAsShowed()
        }
    }

    fun addViewToAppBar(view: View) = binding.contentMain.appBarLayout.addView(view)
    fun addViewToToolbar(view: View) = binding.contentMain.toolbar.addView(view)
    fun removeViewFromAppBar(view: View) = binding.contentMain.appBarLayout.removeView(view)
    fun removeViewFromToolbar(view: View) = binding.contentMain.toolbar.removeView(view)

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
        if (!PreferencesManager.getBearerToken().isNullOrEmpty() && checkNetworkConnection())
            mViewModel.fetchUser()
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
                if ((supportFragmentManager.fragments.first { it.javaClass == GuestsViewPagerFragment::class.java } as GuestsViewPagerFragment).onBackPressed()) doubleBackPressToExit()
            mCurrentFragmentId == R.id.nav_departures ->
                if ((supportFragmentManager.fragments.first { it.javaClass == DepartureListFragment::class.java } as DepartureListFragment).onBackPressed()) doubleBackPressToExit()
            mCurrentFragmentId == BREVIARY_FRAGMENT_ID ->
                goToSelectedFragment(R.id.nav_breviary)
            mCurrentFragmentId!! < 0 ->
                onNavigationItemSelected(binding.navView.menu.getItem(-1 * mCurrentFragmentId!! - 1))
            else -> doubleBackPressToExit()
        }
    }

    private fun doubleBackPressToExit() {
        if (mBackPressed + 2000 > System.currentTimeMillis()) super.onBackPressed()
        else Toast.makeText(this, getString(R.string.press_to_exit), Toast.LENGTH_SHORT).show()
        mBackPressed = System.currentTimeMillis()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        when {
            PreferencesManager.getBearerToken().isNullOrEmpty() -> {
                menu.findItem(R.id.action_sign_in)?.isVisible = true
                menu.findItem(R.id.action_sign_out)?.isVisible = false
            }
            else -> {
                menu.findItem(R.id.action_sign_in)?.isVisible = false
                menu.findItem(R.id.action_sign_out)?.isVisible = true
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mCurrentFragmentId = item.itemId
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        binding.navView.setCheckedItem(mCurrentFragmentId!!)
        val fragment = when (item.itemId) {
            R.id.nav_home -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_home)
                HomeFragment()
            }
            R.id.nav_archive -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_archive)
                ArchiveFragment()
            }
            R.id.nav_schedule -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_schedule)
                ScheduleFragment()
            }
            R.id.nav_anthem -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_anthem)
                AnthemFragment()
            }
            R.id.nav_songbook -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_songbook)
                SongBookFragment()
            }
            R.id.nav_group -> {
                if (PreferencesManager.getBearerToken().isNullOrEmpty()) {
                    openLoginActivity()
                    null
                } else {
                    binding.contentMain.toolbar.title = getString(R.string.menu_group)
                    GroupFragment()
                }
            }
            R.id.nav_showers -> {
                if (PreferencesManager.getBearerToken().isNullOrEmpty()) {
                    openLoginActivity()
                    null
                } else {
                    binding.contentMain.toolbar.title = getString(R.string.menu_showers)
                    ShowersFragment()
                }
            }
            R.id.nav_guests -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_guests)
                GuestsViewPagerFragment()
            }
            R.id.nav_breviary -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_breviary)
                BreviarySelectFragment {
                    mCurrentFragmentId = BREVIARY_FRAGMENT_ID
                    binding.contentMain.toolbar.title =
                        resources.getStringArray(R.array.breviary_list)[it]
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.navHostFragment, BreviaryFragment.newInstance(it))
                        .commit()
                }
            }
            R.id.nav_weather -> {
                binding.contentMain.toolbar.title = getString(R.string.weather_title, "Wołczyn")
                WeatherFragment()
            }
            R.id.nav_signings -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_signings)
                SigningsFragment()
            }
            R.id.nav_departures -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_departures)
                DepartureListFragment()
            }
            R.id.nav_map -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_map)
                MapFragment()
            }
            R.id.nav_settings -> {
                binding.contentMain.toolbar.title = getString(R.string.preferences)
                PreferenceFragment()
            }
            else -> {
                binding.contentMain.toolbar.title = getString(R.string.menu_schedule)
                ScheduleFragment()
            }
        }

        fragment?.let {
            if (mIsTransactionSafe)
                supportFragmentManager.beginTransaction().replace(R.id.navHostFragment, it).commit()
            else mIsTransactionPending = true
        }
        return true
    }

    private fun signOutUser() {
        PreferencesManager.setBearerToken("")
        (binding.navView.findViewById(R.id.headerDrawerImage) as ImageView?)?.setImageResource(R.drawable.ic_logo)
        (binding.navView.findViewById(R.id.headerDrawerName) as TextView?)?.text = ""
        invalidateOptionsMenu()
        if (mCurrentFragmentId == R.id.nav_group || mCurrentFragmentId == R.id.nav_showers)
            goBackToHome()
    }

    private val openLoginActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val fragmentExtra = it.data!!.getIntExtra("fragment", 0)
                if (!it.data!!.getBooleanExtra("loginSuccess", false)
                    && (fragmentExtra == R.id.nav_group || fragmentExtra == R.id.nav_showers)
                ) goBackToHome()
                else goToSelectedFragment(fragmentExtra)
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
        binding.contentMain.toolbar.title = newTitle
    }

    fun goBackToHome() = onNavigationItemSelected(binding.navView.menu.getItem(0))

    fun goToSelectedFragment(fragmentId: Int): Boolean =
        onNavigationItemSelected(binding.navView.menu.findItem(fragmentId))
}
