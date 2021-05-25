package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import pl.kapucyni.wolczyn.app.databinding.FragmentArchiveBinding
import pl.kapucyni.wolczyn.app.utils.checkNetworkConnection
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialogDataOutOfDate
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.view.adapters.ArchiveRecyclerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.ArchiveViewModel

class ArchiveFragment : Fragment() {

    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!

    private lateinit var mArchiveViewModel: ArchiveViewModel
    private lateinit var mAdapter: ArchiveRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = ArchiveRecyclerAdapter(activity as MainActivity)
        binding.archiveRecyclerView.layoutManager = LinearLayoutManager(view.context)
        binding.archiveRecyclerView.itemAnimator = DefaultItemAnimator()
        binding.archiveRecyclerView.adapter = mAdapter

        mArchiveViewModel =
            ViewModelProvider(this@ArchiveFragment).get(ArchiveViewModel::class.java)
        activity?.let { if (!it.checkNetworkConnection()) it.showNoInternetDialogDataOutOfDate() }
        mArchiveViewModel.fetchMeetings()
        mArchiveViewModel.meetings.observe(viewLifecycleOwner, { meetings ->
            mAdapter.setMeetingsList(meetings.sortedByDescending { it.number })
            binding.archiveRecyclerView.scheduleLayoutAnimation()
            binding.loadingIndicator.hide()
            binding.emptyView.visibility =
                if (meetings.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
