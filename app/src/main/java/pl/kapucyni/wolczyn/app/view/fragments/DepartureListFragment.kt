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
import pl.kapucyni.wolczyn.app.R
import pl.kapucyni.wolczyn.app.databinding.FragmentDeparturesBinding
import pl.kapucyni.wolczyn.app.model.Departure
import pl.kapucyni.wolczyn.app.utils.tryToRunFunctionOnInternet
import pl.kapucyni.wolczyn.app.view.adapters.DeparturesRecyclerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.DeparturesViewModel

class DepartureListFragment : Fragment() {

    private var _binding: FragmentDeparturesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mDeparturesViewModel: DeparturesViewModel
    private lateinit var mAdapter: DeparturesRecyclerAdapter
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>
    var selectedDeparture: Departure? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeparturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = DeparturesRecyclerAdapter(this@DepartureListFragment)
        binding.departuresRecyclerView.layoutManager = LinearLayoutManager(view.context)
        binding.departuresRecyclerView.itemAnimator = DefaultItemAnimator()
        binding.departuresRecyclerView.adapter = mAdapter

        mDeparturesViewModel =
            ViewModelProvider(this@DepartureListFragment).get(DeparturesViewModel::class.java)
        fetchDepartures()
        mDeparturesViewModel.departures.observe(viewLifecycleOwner, { departures ->
            departures.sortBy { it.city }
            mAdapter.setDeparturesList(departures)
            binding.departuresRecyclerView.scheduleLayoutAnimation()
            binding.loadingIndicator.hide()
            binding.departuresSwipeToRefresh.isRefreshing = false
            binding.emptyView.visibility =
                if (departures.isEmpty()) View.VISIBLE else View.INVISIBLE
        })

        mBottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.departureSheet))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
