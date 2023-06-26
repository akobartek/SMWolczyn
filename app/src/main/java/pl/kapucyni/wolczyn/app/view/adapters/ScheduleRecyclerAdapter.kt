package pl.kapucyni.wolczyn.app.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.ItemEventBinding
import pl.kapucyni.wolczyn.app.databinding.ItemEventHeaderBinding
import pl.kapucyni.wolczyn.app.model.Event
import pl.kapucyni.wolczyn.app.model.EventPlace
import pl.kapucyni.wolczyn.app.model.EventType
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService
import pl.kapucyni.wolczyn.app.view.fragments.ScheduleFragment

class ScheduleRecyclerAdapter(
    private var mEventsList: ArrayList<Any>,
    private val mFragment: ScheduleFragment
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> ScheduleHeaderViewHolder(
                ItemEventHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            TYPE_ITEM -> ScheduleViewHolder(
                ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw IllegalStateException("There is no type like this!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ScheduleViewHolder) holder.bindView(mEventsList[position] as Event)
        else (holder as ScheduleHeaderViewHolder).bindView(mEventsList[position] as String)
    }

    override fun getItemViewType(position: Int): Int =
        if (mEventsList[position] is Event) TYPE_ITEM else TYPE_HEADER

    override fun getItemCount(): Int = mEventsList.size

    inner class ScheduleHeaderViewHolder(private val binding: ItemEventHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindView(header: String) {
            binding.eventHeader.text = header.split("~")[0]
            binding.eventHeaderName.text = "\"${header.split("~")[1]}\""
        }
    }

    inner class ScheduleViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(event: Event) {
            with(binding) {
                eventName.text = event.name
                eventLocation.text = root.context.getString(
                    when (event.eventPlace) {
                        EventPlace.AMPHITHEATRE -> R.string.place_amphitheatre
                        EventPlace.WHITE_TENT -> R.string.place_white_tent
                        EventPlace.GARDEN -> R.string.place_garden
                        EventPlace.CAMPSITE -> R.string.place_campsite
                        EventPlace.COURT -> R.string.place_court
                        EventPlace.EVERYWHERE -> R.string.place_everywhere
                        EventPlace.CHURCH -> R.string.place_church
                        EventPlace.UNKNOWN -> R.string.place_unknown
                    }
                )
                eventType.text = root.context.getString(
                    when (event.eventType) {
                        EventType.CONFERENCE -> R.string.type_conference
                        EventType.CONCERT -> R.string.type_concert
                        EventType.MASS -> R.string.type_mass
                        EventType.DEVOTION -> R.string.type_devotion
                        EventType.PRAYER -> R.string.type_prayer
                        EventType.ORGANIZATION -> R.string.type_organization
                        EventType.MEAL -> R.string.type_meal
                        EventType.OTHER -> R.string.type_other
                        EventType.GROUPS -> R.string.type_groups
                        EventType.WORKSHOPS -> R.string.type_workshops
                        EventType.BREVIARY -> R.string.type_breviary
                        EventType.EXTRA -> R.string.type_extra
                    }
                )
                val color = when (event.eventType) {
                    EventType.CONFERENCE, EventType.CONCERT -> R.color.colorEventType1
                    EventType.MASS, EventType.DEVOTION, EventType.PRAYER -> R.color.colorEventType2
                    EventType.ORGANIZATION, EventType.MEAL, EventType.OTHER -> R.color.colorEventType3
                    EventType.GROUPS, EventType.WORKSHOPS -> R.color.colorEventType4
                    EventType.BREVIARY, EventType.EXTRA -> R.color.colorEventType5
                }
                DrawableCompat.setTint(
                    DrawableCompat.wrap(eventTypeColor.drawable),
                    ContextCompat.getColor(root.context, color)
                )

                videoImage.visibility =
                    if (event.videoUrl.isNullOrEmpty()) View.GONE else View.VISIBLE
                videoImage.setOnClickListener {
                    itemView.context.openWebsiteInCustomTabsService(event.videoUrl!!)
                }

                root.setOnClickListener { mFragment.onItemClick(event) }
            }
        }
    }
}