package pl.kapucyni.wolczyn.app.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.kapucyni.wolczyn.app.databinding.ItemDepartureBinding
import pl.kapucyni.wolczyn.app.model.Departure
import pl.kapucyni.wolczyn.app.view.fragments.DepartureListFragment

class DeparturesRecyclerAdapter(private val mFragment: DepartureListFragment) :
    RecyclerView.Adapter<DeparturesRecyclerAdapter.DepartureViewHolder>() {

    private var mDeparturesList = listOf<Departure>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartureViewHolder =
        DepartureViewHolder(
            ItemDepartureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: DepartureViewHolder, position: Int) =
        holder.bindView(mDeparturesList[position])

    override fun getItemCount(): Int = mDeparturesList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setDeparturesList(list: List<Departure>) {
        mDeparturesList = list
        notifyDataSetChanged()
    }

    inner class DepartureViewHolder(private val binding: ItemDepartureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(departure: Departure) {
            with(binding) {
                departureCity.text = departure.city
                departurePatron.text = departure.patron
                departureTransportType.setImageResource(
                    mFragment.getDepartureTransportTypeImageResource(departure.transport_type)
                )
                root.setOnClickListener {
                    if (mFragment.selectedDeparture == null) mFragment.expandBottomSheet(departure)
                    else mFragment.hideBottomSheet()
                }
            }
        }
    }
}