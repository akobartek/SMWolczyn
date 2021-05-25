package pl.kapucyni.wolczyn.app.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.kapucyni.wolczyn.app.databinding.ItemArchiveRecordBinding
import pl.kapucyni.wolczyn.app.model.Record
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService
import java.util.*

class ArchiveMeetingsRecyclerAdapter(private val mRecords: ArrayList<Record>?) :
    RecyclerView.Adapter<ArchiveMeetingsRecyclerAdapter.ArchiveMeetingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveMeetingViewHolder =
        ArchiveMeetingViewHolder(
            ItemArchiveRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ArchiveMeetingViewHolder, position: Int) =
        holder.bindView(mRecords?.get(position) ?: Record())

    override fun getItemCount(): Int = mRecords?.size ?: 0

    inner class ArchiveMeetingViewHolder(private val binding: ItemArchiveRecordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(record: Record) {
            with(binding) {
                recordName.text = record.name.replace("<n>", "\n")

                recordVideoBtn.setOnClickListener {
                    itemView.context.openWebsiteInCustomTabsService(record.videoUrl)
                }
                root.setOnClickListener {
                    itemView.context.openWebsiteInCustomTabsService(record.videoUrl)
                }
            }
        }
    }
}