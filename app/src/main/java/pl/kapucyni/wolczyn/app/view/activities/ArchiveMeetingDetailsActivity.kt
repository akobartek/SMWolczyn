package pl.kapucyni.wolczyn.app.view.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_archive_meeting_details.*
import kotlinx.android.synthetic.main.content_archive_meeting_details.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.utils.PreferencesManager
import pl.kapucyni.wolczyn.app.view.adapters.ArchiveMeetingsRecyclerAdapter

class ArchiveMeetingDetailsActivity : AppCompatActivity() {

    private lateinit var mAdapter: ArchiveMeetingsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_meeting_details)
        setSupportActionBar(meetingToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        title = intent.getStringExtra("title")

        if (!PreferencesManager.getNightMode() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        mAdapter = ArchiveMeetingsRecyclerAdapter(intent.getParcelableArrayListExtra("records"))
        recordsRecyclerView.layoutManager = LinearLayoutManager(this@ArchiveMeetingDetailsActivity)
        recordsRecyclerView.itemAnimator = DefaultItemAnimator()
        recordsRecyclerView.addItemDecoration(
            DividerItemDecoration(this@ArchiveMeetingDetailsActivity, DividerItemDecoration.VERTICAL)
        )
        recordsRecyclerView.adapter = mAdapter
        recordsRecyclerView.scheduleLayoutAnimation()
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
