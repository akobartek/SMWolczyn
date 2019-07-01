package pl.kapucyni.wolczyn.app.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_event.view.*
import kotlinx.android.synthetic.main.item_event_header.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.model.Event
import pl.kapucyni.wolczyn.app.model.EventPlace
import pl.kapucyni.wolczyn.app.model.EventType
import pl.kapucyni.wolczyn.app.utils.openWebsiteInCustomTabsService
import pl.kapucyni.wolczyn.app.view.fragments.ScheduleFragment

class ScheduleRecyclerAdapter(private var mEventsList: ArrayList<Any>, private val mFragment: ScheduleFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> ScheduleHeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_event_header,
                    parent,
                    false
                )
            )
            TYPE_ITEM -> ScheduleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_event,
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("There is no type like this!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ScheduleViewHolder) holder.bindView(mEventsList[position] as Event)
        else (holder as ScheduleHeaderViewHolder).bindView(mEventsList[position] as String)
    }

    override fun getItemViewType(position: Int): Int = if (mEventsList[position] is Event) TYPE_ITEM else TYPE_HEADER

    override fun getItemCount(): Int = mEventsList.size

    inner class ScheduleHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(header: String) {
            itemView.eventHeader.text = header
        }
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(event: Event) {
            itemView.eventName.text = event.name
            itemView.eventLocation.text = when (event.eventPlace) {
                EventPlace.AMPHITHEATRE -> itemView.context.getString(R.string.place_amphitheatre)
                EventPlace.WHITE_TENT -> itemView.context.getString(R.string.place_white_tent)
                EventPlace.GARDEN -> itemView.context.getString(R.string.place_garden)
                EventPlace.CAMPSITE -> itemView.context.getString(R.string.place_campsite)
                EventPlace.COURT -> itemView.context.getString(R.string.place_court)
                EventPlace.EVERYWHERE -> itemView.context.getString(R.string.place_everywhere)
            }
            itemView.eventType.text = when (event.eventType) {
                EventType.CONFERENCE -> itemView.context.getString(R.string.type_conference)
                EventType.CONCERT -> itemView.context.getString(R.string.type_concert)
                EventType.MASS -> itemView.context.getString(R.string.type_mass)
                EventType.DEVOTION -> itemView.context.getString(R.string.type_devotion)
                EventType.PRAYER -> itemView.context.getString(R.string.type_prayer)
                EventType.ORGANIZATION -> itemView.context.getString(R.string.type_organization)
                EventType.MEAL -> itemView.context.getString(R.string.type_meal)
                EventType.OTHER -> itemView.context.getString(R.string.type_other)
                EventType.GROUPS -> itemView.context.getString(R.string.type_groups)
                EventType.BREVIARY -> itemView.context.getString(R.string.type_breviary)
                EventType.EXTRA -> itemView.context.getString(R.string.type_extra)
            }
            val color = when (event.eventType) {
                EventType.CONFERENCE, EventType.CONCERT -> R.color.colorEventType1
                EventType.MASS, EventType.DEVOTION, EventType.PRAYER -> R.color.colorEventType2
                EventType.ORGANIZATION, EventType.MEAL, EventType.OTHER -> R.color.colorEventType3
                EventType.GROUPS -> R.color.colorEventType4
                EventType.BREVIARY, EventType.EXTRA -> R.color.colorEventType5
            }
            DrawableCompat.setTint(
                DrawableCompat.wrap(itemView.eventTypeColor.drawable),
                ContextCompat.getColor(itemView.context, color)
            )

            itemView.videoImage.visibility = if (event.videoUrl.isNullOrEmpty()) View.GONE else View.VISIBLE
            itemView.videoImage.setOnClickListener {
                itemView.context.openWebsiteInCustomTabsService(event.videoUrl!!)
            }

            itemView.setOnClickListener { mFragment.onItemClick(event) }
        }
    }
}