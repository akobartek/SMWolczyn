package pl.kapucyni.wolczyn.app.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentDeparturesBinding
import pl.kapucyni.wolczyn.app.model.Departure
import pl.kapucyni.wolczyn.app.utils.tryToRunFunctionOnInternet
import pl.kapucyni.wolczyn.app.view.adapters.DeparturesRecyclerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.DeparturesViewModel

class DepartureListFragment : BindingFragment<FragmentDeparturesBinding>() {

    private val mDeparturesViewModel: DeparturesViewModel by activityViewModels()
    private lateinit var mAdapter: DeparturesRecyclerAdapter
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    var selectedDeparture: Departure? = null

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDeparturesBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        mAdapter = DeparturesRecyclerAdapter(this@DepartureListFragment)
        binding.departuresRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        fetchDepartures()
        mDeparturesViewModel.departures.observe(viewLifecycleOwner) { departures ->
            departures.sortBy { it.city }
            mAdapter.setDeparturesList(departures)
            binding.departuresRecyclerView.scheduleLayoutAnimation()
            binding.loadingIndicator.hide()
            binding.departuresSwipeToRefresh.isRefreshing = false
            binding.emptyView.visibility =
                if (departures.isEmpty()) View.VISIBLE else View.INVISIBLE
        }

        mBottomSheetBehavior = BottomSheetBehavior.from(binding.departureSheet)
        mBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("SetTextI18n")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                val departureFragment =
                    childFragmentManager.findFragmentById(R.id.departureSheet) as DepartureDetailsFragment
                selectedDeparture?.let {
                    departureFragment.setViewsValues(
                        it, getDepartureTransportTypeImageResource(it.transport_type)
                    )
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.departuresRecyclerView.alpha = 1f
                    binding.departuresRecyclerView.isEnabled = true
                    selectedDeparture = null
                    departureFragment.hideViews()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.departuresSwipeToRefresh.apply {
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
        requireActivity().tryToRunFunctionOnInternet { mDeparturesViewModel.fetchDepartures() }
    }

    fun expandBottomSheet(departure: Departure) {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        selectedDeparture = departure
        binding.departuresRecyclerView.isEnabled = false
        binding.departuresRecyclerView.animate().alpha(0.15f).duration = 200
    }

    fun hideBottomSheet() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun onBackPressed(): Boolean =
        if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN
            || mBottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED
        ) true
        else {
            hideBottomSheet()
            false
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
