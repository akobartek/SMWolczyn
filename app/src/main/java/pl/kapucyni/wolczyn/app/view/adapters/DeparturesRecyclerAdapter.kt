package pl.kapucyni.wolczyn.app.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_departure.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.model.Departure
import pl.kapucyni.wolczyn.app.view.fragments.DepartureListFragment

class DeparturesRecyclerAdapter(private val mFragment: DepartureListFragment) :
    RecyclerView.Adapter<DeparturesRecyclerAdapter.DepartureViewHolder>() {

    private var mDeparturesList = listOf<Departure>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartureViewHolder =
        DepartureViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_departure, parent, false)
        )

    override fun onBindViewHolder(holder: DepartureViewHolder, position: Int) =
        holder.bindView(mDeparturesList[position])

    override fun getItemCount(): Int = mDeparturesList.size

    fun setDeparturesList(list: List<Departure>) {
        mDeparturesList = list
        notifyDataSetChanged()
    }

    inner class DepartureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(departure: Departure) {
            itemView.departureCity.text = departure.city
            itemView.departurePatron.text = departure.patron
            itemView.departureTransportType.setImageResource(
                mFragment.getDepartureTransportTypeImageResource(departure.transport_type)
            )

            itemView.setOnClickListener {
                if (mFragment.selectedDeparture == null) mFragment.expandBottomSheet(departure)
                else mFragment.hideBottomSheet()
            }
        }
    }
}