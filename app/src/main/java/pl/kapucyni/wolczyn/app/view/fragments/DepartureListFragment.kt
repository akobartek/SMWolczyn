package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_departures.view.*
import kotlinx.android.synthetic.main.sheet_fragment_departure_details.view.*
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.model.Departure
import pl.kapucyni.wolczyn.app.utils.tryToRunFunctionOnInternet
import pl.kapucyni.wolczyn.app.view.adapters.DeparturesRecyclerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.DeparturesViewModel

class DepartureListFragment : Fragment() {

    private lateinit var mDeparturesViewModel: DeparturesViewModel
    private lateinit var mAdapter: DeparturesRecyclerAdapter
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    var selectedDeparture: Departure? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_departures, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = DeparturesRecyclerAdapter(this@DepartureListFragment)
        view.departuresRecyclerView.layoutManager = LinearLayoutManager(view.context)
        view.departuresRecyclerView.itemAnimator = DefaultItemAnimator()
        view.departuresRecyclerView.adapter = mAdapter

        mDeparturesViewModel = ViewModelProvider(this@DepartureListFragment).get(DeparturesViewModel::class.java)
        fetchDepartures()
        mDeparturesViewModel.departures.observe(viewLifecycleOwner, { departures ->
            departures.sortBy { it.city }
            mAdapter.setDeparturesList(departures)
            view.departuresRecyclerView.scheduleLayoutAnimation()
            view.loadingIndicator.hide()
            view.departuresSwipeToRefresh.isRefreshing = false
            if (departures.isEmpty()) {
                view.emptyView.visibility = View.VISIBLE
            } else {
                view.emptyView.visibility = View.INVISIBLE
            }
        })

        mBottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.departureSheet))
        mBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SetTextI18n")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                selectedDeparture?.let {
                    bottomSheet.departureTransportType.setImageResource(getDepartureTransportTypeImageResource(it.transport_type))
                    bottomSheet.departureCity.text = it.city
                    bottomSheet.departurePatron.text = getString(R.string.organizer, it.patron)
                    it.direction?.let { direction ->
                        if (direction.isNotEmpty()) {
                            bottomSheet.departureDirection.text = "($direction)"
                            bottomSheet.departureDirection.visibility = View.VISIBLE
                        }
                    }
                    it.notes?.let { notes ->
                        if (notes.isNotEmpty()) {
                            bottomSheet.departureNotes.text = notes
                            bottomSheet.departureNotes.visibility = View.VISIBLE
                        }
                    }
                    it.contact_phone?.let { phone ->
                        if (phone.isNotEmpty()) {
                            bottomSheet.departurePhone.text = phone
                            bottomSheet.departurePhone.visibility = View.VISIBLE
                        }
                    }
                    it.contact_email?.let { email ->
                        if (email.isNotEmpty()) {
                            bottomSheet.departureEmail.text = email
                            bottomSheet.departureEmail.visibility = View.VISIBLE
                        }
                    }
                    it.signin_url?.let { url ->
                        if (url.isNotEmpty()) {
                            bottomSheet.departureSigningUrl.text = url
                            bottomSheet.departureSigningUrl.visibility = View.VISIBLE
                        }
                    }
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    view.departuresRecyclerView.alpha = 1f
                    view.departuresRecyclerView.isEnabled = true
                    selectedDeparture = null
                    bottomSheet.departureTransportType.setImageResource(android.R.color.transparent)
                    bottomSheet.departureCity.text = ""
                    bottomSheet.departurePatron.text = ""
                    bottomSheet.departureDirection.visibility = View.GONE
                    bottomSheet.departureNotes.visibility = View.GONE
                    bottomSheet.departurePhone.visibility = View.GONE
                    bottomSheet.departureEmail.visibility = View.GONE
                    bottomSheet.departureSigningUrl.visibility = View.GONE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        view.departuresSwipeToRefresh.apply {
            setOnRefreshListener { fetchDepartures() }
            setOnClickListener { if (selectedDeparture != null) hideBottomSheet() }
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.swipe_refresh_color_1),
                ContextCompat.getColor(context, R.color.swipe_refresh_color_2),
                ContextCompat.getColor(context, R.color.swipe_refresh_color_3),
                ContextCompat.getColor(context, R.color.swipe_refresh_color_4)
            )
        }
    }

    private fun fetchDepartures() {
        activity?.tryToRunFunctionOnInternet { mDeparturesViewModel.fetchDepartures() }
    }

    fun expandBottomSheet(departure: Departure) {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        selectedDeparture = departure
        view?.departuresRecyclerView?.isEnabled = false
        view?.departuresRecyclerView?.animate()
            ?.alpha(0.15f)
            ?.duration = 200
    }

    fun hideBottomSheet() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun onBackPressed(): Boolean {
        return if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN || mBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            true
        } else {
            hideBottomSheet()
            false
        }
    }

    fun getDepartureTransportTypeImageResource(transportType: String?) = when {
        transportType == null -> R.drawable.ic_foot
        transportType.contains("bus") -> R.drawable.ic_bus
        transportType.contains("PKS") -> R.drawable.ic_bus
        transportType.contains("pociąg") -> R.drawable.ic_train
        transportType.contains("PKP") -> R.drawable.ic_train
        transportType.contains("auto") -> R.drawable.ic_car
        transportType.contains("samoch") -> R.drawable.ic_car
        else -> R.drawable.ic_foot
    }
}
