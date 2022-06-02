package pl.kapucyni.wolczyn.app.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.elevation.SurfaceColors
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.ActivityArchiveMeetingDetailsBinding
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService
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

        val color = SurfaceColors.SURFACE_2.getColor(this)
        window.statusBarColor = color
        window.navigationBarColor = color

        mAdapter = ArchiveMeetingsRecyclerAdapter(intent.getParcelableArrayListExtra("records"))
        binding.archiveLayout.recordsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ArchiveMeetingDetailsActivity)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(
                DividerItemDecoration(
                    this@ArchiveMeetingDetailsActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = mAdapter
            scheduleLayoutAnimation()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!intent.getStringExtra("anthem").isNullOrEmpty())
            menuInflater.inflate(R.menu.menu_anthem, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_yt_anthem -> {
                openWebsiteInCustomTabsService(intent.getStringExtra("anthem") ?: "")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
