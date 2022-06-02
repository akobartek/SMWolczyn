package pl.kapucyni.wolczyn.app.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.ActivityArchiveMeetingDetailsBinding
import pl.kapucyni.wolczyn.app.utils.PreferencesManager
import pl.kapucyni.wolczyn.app.view.adapters.ArchiveMeetingsRecyclerAdapter

class ArchiveMeetingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchiveMeetingDetailsBinding
    private lateinit var mAdapter: ArchiveMeetingsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchiveMeetingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.meetingToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        title = intent.getStringExtra("title")

        val wic = WindowInsetsControllerCompat(window, window.decorView)
        wic.isAppearanceLightStatusBars = !PreferencesManager.getNightMode()
        wic.isAppearanceLightNavigationBars = !PreferencesManager.getNightMode()
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_theme_background)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.app_theme_background)

        mAdapter = ArchiveMeetingsRecyclerAdapter(intent.getParcelableArrayListExtra("records"))
        binding.archiveLayout.recordsRecyclerView.layoutManager =
            LinearLayoutManager(this@ArchiveMeetingDetailsActivity)
        binding.archiveLayout.recordsRecyclerView.itemAnimator = DefaultItemAnimator()
        binding.archiveLayout.recordsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this@ArchiveMeetingDetailsActivity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.archiveLayout.recordsRecyclerView.adapter = mAdapter
        binding.archiveLayout.recordsRecyclerView.scheduleLayoutAnimation()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
