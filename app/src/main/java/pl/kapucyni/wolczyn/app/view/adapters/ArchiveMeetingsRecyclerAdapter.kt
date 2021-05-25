package pl.kapucyni.wolczyn.app.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_archive_record.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.model.Record
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService
import java.util.*

class ArchiveMeetingsRecyclerAdapter(private val mRecords: ArrayList<Record>?) :
    RecyclerView.Adapter<ArchiveMeetingsRecyclerAdapter.ArchiveMeetingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveMeetingViewHolder =
        ArchiveMeetingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_archive_record, parent, false)
        )

    override fun onBindViewHolder(holder: ArchiveMeetingViewHolder, position: Int) =
        holder.bindView(mRecords?.get(position) ?: Record())

    override fun getItemCount(): Int = mRecords?.size ?: 0

    inner class ArchiveMeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(record: Record) {
            itemView.recordName.text = record.name.replace("<n>", "\n")

            itemView.recordVideoBtn.setOnClickListener { itemView.context.openWebsiteInCustomTabsService(record.videoUrl) }
            itemView.setOnClickListener { itemView.context.openWebsiteInCustomTabsService(record.videoUrl) }
        }
    }
}