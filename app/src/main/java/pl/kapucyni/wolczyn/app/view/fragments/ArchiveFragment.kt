package pl.kapucyni.wolczyn.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import pl.kapucyni.wolczyn.app.databinding.FragmentArchiveBinding
import pl.kapucyni.wolczyn.app.utils.checkNetworkConnection
import pl.kapucyni.wolczyn.app.utils.showNoInternetDialogDataOutOfDate
import pl.kapucyni.wolczyn.app.view.activities.MainActivity
import pl.kapucyni.wolczyn.app.view.adapters.ArchiveRecyclerAdapter
import pl.kapucyni.wolczyn.app.viewmodels.ArchiveViewModel

class ArchiveFragment : BindingFragment<FragmentArchiveBinding>() {

    private lateinit var mArchiveViewModel: ArchiveViewModel
    private lateinit var mAdapter: ArchiveRecyclerAdapter

    override fun attachBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentArchiveBinding.inflate(inflater, container, false)

    override fun setup(savedInstanceState: Bundle?) {
        mAdapter = ArchiveRecyclerAdapter(activity as MainActivity)
        binding.archiveRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        mArchiveViewModel =
            ViewModelProvider(this@ArchiveFragment)[ArchiveViewModel::class.java]
        requireActivity().let { if (!it.checkNetworkConnection()) it.showNoInternetDialogDataOutOfDate() }
        mArchiveViewModel.fetchMeetings()
        mArchiveViewModel.meetings.observe(viewLifecycleOwner) { meetings ->
            mAdapter.setMeetingsList(meetings.sortedByDescending { it.number })
            binding.archiveRecyclerView.scheduleLayoutAnimation()
            binding.loadingIndicator.hide()
            binding.emptyView.visibility =
                if (meetings.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }
}
