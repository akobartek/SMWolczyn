package pl.kapucyni.wolczyn.app.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.ItemArchiveBinding
import pl.kapucyni.wolczyn.app.model.ArchiveMeeting
import pl.kapucyni.wolczyn.app.utils.GlideApp
import pl.kapucyni.wolczyn.app.view.activities.MainActivity

class ArchiveRecyclerAdapter(val activity: MainActivity) :
    RecyclerView.Adapter<ArchiveRecyclerAdapter.ArchiveMeetingViewHolder>() {

    private var mMeetings = listOf<ArchiveMeeting>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveMeetingViewHolder =
        ArchiveMeetingViewHolder(
            ItemArchiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ArchiveMeetingViewHolder, position: Int) =
        holder.bindView(mMeetings[position])

    override fun getItemCount(): Int = mMeetings.size

    fun setWeatherList(list: List<ArchiveMeeting>) {
        mMeetings = list
        notifyDataSetChanged()
    }

    inner class ArchiveMeetingViewHolder(private val binding: ItemArchiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(meeting: ArchiveMeeting) {
            with(binding) {
                GlideApp.with(root.context)
                    .load(meeting.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(meetingPhoto)
                meetingName.text = meeting.name

                root.setOnClickListener {
                    if (meeting.records.isNotEmpty()) activity.openArchiveDetailsActivity(meeting)
                    else showEmptyRecordsListDialog()
                }
            }
        }

        private fun showEmptyRecordsListDialog() =
            AlertDialog.Builder(binding.root.context)
                .setTitle(R.string.no_records_dialog_title)
                .setMessage(R.string.no_records_dialog_message)
                .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
    }
}