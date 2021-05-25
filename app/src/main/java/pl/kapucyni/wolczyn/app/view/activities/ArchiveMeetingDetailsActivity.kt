package pl.kapucyni.wolczyn.app.view.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

        if (!PreferencesManager.getNightMode()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS
                )
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.statusBarColor = Color.WHITE
            }
        }

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
